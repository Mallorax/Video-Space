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
import pl.patrykzygo.videospace.data.local.GenreDao
import pl.patrykzygo.videospace.data.local.GenreEntity
import pl.patrykzygo.videospace.data.mapGenreEntityToGenre
import pl.patrykzygo.videospace.data.mapGenreItemToGenreNullable
import pl.patrykzygo.videospace.fakeCorrectGenresResponse
import pl.patrykzygo.videospace.fakeGenreEntitiesList
import pl.patrykzygo.videospace.fakeHttpErrorResponse
import pl.patrykzygo.videospace.networking.GenresEntryPoint
import pl.patrykzygo.videospace.repository.RepositoryResponse

@ExperimentalCoroutinesApi
class LocalStoreGenresRepositoryImplTest {

    @Mock
    private lateinit var mockGenreDao: GenreDao

    @Mock
    private lateinit var mockGenresEntryPoint: GenresEntryPoint

    private lateinit var repo: LocalStoreGenresRepository


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repo = LocalStoreGenresRepositoryImpl(mockGenreDao, mockGenresEntryPoint)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `getAllGenres returns success with local db populated`() =
        runTest {
            val fakeEntities = fakeGenreEntitiesList()
            Mockito.`when`(mockGenreDao.getGenres()).thenAnswer {
                fakeEntities
            }
            val expected = fakeEntities.map { mapGenreEntityToGenre(it) }
            val result = repo.getAllGenres().data
            assertThat(result).containsExactlyElementsIn(expected)
        }

    @Test
    fun `getAllGenres returns success with empty local db`() = runTest {
        Mockito.`when`(mockGenreDao.getGenres()).thenAnswer {
            listOf<GenreEntity>()
        }
        val fakeGenresResponse = fakeCorrectGenresResponse()
        Mockito.`when`(mockGenresEntryPoint.getGenresForMovies()).thenAnswer {
            fakeGenresResponse
        }
        val expected =
            fakeGenresResponse.body()?.genres?.mapNotNull { mapGenreItemToGenreNullable(it) }
        val result = repo.getAllGenres()
        assertThat(result.data).containsExactlyElementsIn(expected)
    }

    @Test
    fun `getAllGenres returns error with empty local db`() = runTest {
        Mockito.`when`(mockGenreDao.getGenres()).thenAnswer {
            listOf<GenreEntity>()
        }
        val fakeGenresResponseError = fakeHttpErrorResponse()
        Mockito.`when`(mockGenresEntryPoint.getGenresForMovies()).thenAnswer {
            fakeGenresResponseError
        }
        val expected = RepositoryResponse.Status.ERROR
        val result = repo.getAllGenres()
        assertThat(result.status).isEqualTo(expected)
    }

    @Test
    fun `getAllGenres returns error`() = runTest {
        val result = repo.getAllGenres()
        val expected = RepositoryResponse.Status.ERROR
        assertThat(result.status).isEqualTo(expected)
    }

    @Test
    fun `getGenreId returns success with local db populated`() = runTest {
        Mockito.`when`(mockGenreDao.getGenres()).thenAnswer {
            fakeGenreEntitiesList()
        }
        val result = repo.getGenreId("1")
        assertThat(result.data).isEqualTo(1)
    }

    @Test
    fun `getGenreId returns success with empty local db`() = runTest {
        Mockito.`when`(mockGenreDao.getGenres()).thenAnswer {
            listOf<GenreEntity>()
        }
        Mockito.`when`(mockGenresEntryPoint.getGenresForMovies()).thenAnswer {
            fakeCorrectGenresResponse()
        }
        val result = repo.getGenreId("1")
        assertThat(result.data).isEqualTo(1)
    }

    @Test
    fun `getGenreId returns no such genre error`() = runTest {
        Mockito.`when`(mockGenreDao.getGenres()).thenAnswer {
            listOf<GenreEntity>()
        }
        Mockito.`when`(mockGenresEntryPoint.getGenresForMovies()).thenAnswer {
            fakeCorrectGenresResponse()
        }
        val result = repo.getGenreId("10")
        assertThat(result.message).isEqualTo("No such genre")
    }

    @Test
    fun `getGenreId returns error with empty local db returns error`() = runTest {
        Mockito.`when`(mockGenreDao.getGenres()).thenAnswer {
            listOf<GenreEntity>()
        }
        Mockito.`when`(mockGenresEntryPoint.getGenresForMovies()).thenAnswer {
            fakeHttpErrorResponse()
        }
        val result = repo.getGenreId("1")
        assertThat(result.status).isEqualTo(RepositoryResponse.Status.ERROR)
    }

    @Test
    fun `getGenreId returns error`() = runTest {
        val result = repo.getGenreId("1")
        assertThat(result.status).isEqualTo(RepositoryResponse.Status.ERROR)
    }

}