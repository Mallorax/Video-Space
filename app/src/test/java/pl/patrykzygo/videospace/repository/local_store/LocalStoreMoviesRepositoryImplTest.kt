package pl.patrykzygo.videospace.repository.local_store

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import pl.patrykzygo.videospace.constants.MovieStatus
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.local.MoviesDao
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import pl.patrykzygo.videospace.repository.RepositoryResponse
import pl.patrykzygo.videospace.util.fakeHttpErrorResponse
import pl.patrykzygo.videospace.util.fakeMovieDetailsResponse
import pl.patrykzygo.videospace.util.fakeMoviesEntitiesList

@ExperimentalCoroutinesApi
class LocalStoreMoviesRepositoryImplTest {

    @Mock
    private lateinit var mockMoviesDao: MoviesDao

    @Mock
    private lateinit var mockMoviesEntryPoint: MoviesEntryPoint

    private lateinit var repo: LocalStoreMoviesRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repo = LocalStoreMoviesRepositoryImpl(mockMoviesDao, mockMoviesEntryPoint)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAllMoviesFromDb returns success with populated db`() = runTest {
        val expected = fakeMoviesEntitiesList()
        Mockito.`when`(mockMoviesDao.getAllMovies()).thenAnswer {
            expected
        }
        val result = repo.getAllMoviesFromDb()
        assertThat(result.data).isEqualTo(expected)
    }

    @Test
    fun `getAllMoviesFromDb returns no movies repo response`() = runTest {
        Mockito.`when`(mockMoviesDao.getAllMovies()).thenAnswer {
            listOf<MovieEntity>()
        }
        val expected = RepositoryResponse.error<List<MovieEntity>>("No movies saved to show")
        val result = repo.getAllMoviesFromDb()
        assertThat(result.message).isEqualTo(expected.message)
    }

    @Test
    fun `getAllMoviesFromDb catches exception`() = runTest(UnconfinedTestDispatcher()) {
        val expected = RepositoryResponse.Status.ERROR
        val actual = repo.getAllMoviesFromDb()
        assertThat(actual.status).isEqualTo(expected)
    }

    @Test
    fun `getAllMoviesWithStatus returns only movies with correct status`() = runTest {
        val expected = listOf(
            MovieEntity(1, "1", status = MovieStatus.PLAN_TO_WATCH),
            MovieEntity(2, "2", status = MovieStatus.PLAN_TO_WATCH),
            MovieEntity(3, "3", status = MovieStatus.PLAN_TO_WATCH)
        )
        Mockito.`when`(mockMoviesDao.getAllMoviesWithStatus(MovieStatus.PLAN_TO_WATCH)).thenAnswer {
            expected
        }
        val result = repo.getAllMoviesWithStatus(MovieStatus.PLAN_TO_WATCH)
        assertThat(result.data).containsExactlyElementsIn(expected)
    }

    @Test
    fun `getAllMoviesWithStatus returns empty list`() = runTest {
        Mockito.`when`(mockMoviesDao.getAllMoviesWithStatus(MovieStatus.PLAN_TO_WATCH)).thenAnswer {
            listOf<MovieEntity>()
        }
        val result = repo.getAllMoviesWithStatus(MovieStatus.PLAN_TO_WATCH)
        assertThat(result.data).isEmpty()
    }

    @Test
    fun `getAllMoviesWithStatus catches exception`() = runTest {
        Mockito.`when`(mockMoviesDao.getAllMoviesWithStatus(MovieStatus.PLAN_TO_WATCH)).thenAnswer {
            throw Exception()
        }
        val result = repo.getAllMoviesWithStatus(MovieStatus.PLAN_TO_WATCH)
        assertThat(result.status).isEqualTo(RepositoryResponse.Status.ERROR)
    }

    @Test
    fun `getSpecificMovie returns correct movie`() = runTest {
        val expected = fakeMovieDetailsResponse(1)
        Mockito.`when`(mockMoviesEntryPoint.requestMovie(1)).thenAnswer {
            expected
        }
        val result = repo.getSpecificMovie(1)
        assertThat(result.data).isEqualTo(expected.body())
    }

    @Test
    fun `getSpecificMovie returns network error`() = runTest {
        val expected = fakeHttpErrorResponse()
        Mockito.`when`(mockMoviesEntryPoint.requestMovie(1)).thenAnswer {
            expected
        }
        val result = repo.getSpecificMovie(1)
        assertThat(result.message).isEqualTo(expected.message())
    }

    @Test
    fun `getSpecificMovie catches exception`() = runTest {
        val expected = "test exception message"
        Mockito.`when`(mockMoviesEntryPoint.requestMovie(1)).thenAnswer {
            throw Exception(expected)
        }
        val result = repo.getSpecificMovie(1)
        assertThat(result.message).isEqualTo(expected)
    }

    @Test
    fun `getSpecificMovieFromDb returns success`() = runTest {
        val expected = MovieEntity(1, "1")
        Mockito.`when`(mockMoviesDao.getMovieWithId(1)).thenAnswer {
            expected
        }
        val result = repo.getSpecificMovieFromDb(1)
        assertThat(result.data).isEqualTo(expected)
    }

    @Test
    fun `getSpecificMovieFromDb returns no such movie`() = runTest {
        Mockito.`when`(mockMoviesDao.getMovieWithId(1)).thenAnswer { null }
        val result = repo.getSpecificMovieFromDb(1)
        assertThat(result.message).isEqualTo("No such movie")
    }

    @Test
    fun `getSpecificMovieFromDb throws unknown exception`() = runTest {
        val expected = "test exception message"
        Mockito.`when`(mockMoviesDao.getMovieWithId(1)).thenAnswer { throw Exception(expected) }
        val result = repo.getSpecificMovieFromDb(1)
        assertThat(result.message).isEqualTo(expected)
    }
}