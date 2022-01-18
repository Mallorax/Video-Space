package pl.patrykzygo.videospace.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import pl.patrykzygo.videospace.data.network.MoviesResponse
import pl.patrykzygo.videospace.data.network.PopularMoviesResponse
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import pl.patrykzygo.videospace.fakeCorrectMoviesResponse
import pl.patrykzygo.videospace.fakeHttpErrorResponse
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
class MoviesPagingSourceTest {

    private lateinit var moviesPagingSource: MoviesPagingSource

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockMoviesEntryPoint: MoviesEntryPoint
    private val mockVideos = mutableListOf<Response<PopularMoviesResponse>>()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        moviesPagingSource = MoviesPagingSource(mockMoviesEntryPoint)
    }

    @After
    fun teardown() {
        mockVideos.clear()
    }

    @Test
    fun `videos paging source refresh is success test`() = runBlockingTest {
        mockVideos.add(fakeCorrectMoviesResponse(1, 2))

        Mockito.`when`(mockMoviesEntryPoint.requestPopularMovies(page = 1))
            .thenAnswer { mockVideos[0] }

        val actual = moviesPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        val expected = PagingSource.LoadResult.Page(
            data = listOf(mockVideos[0]).flatMap { it.body()!!.moviesList },
            prevKey = null,
            nextKey = mockVideos[0].body()?.page?.plus(1),
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `movies paging source load is http error`() = runBlockingTest {
        val error = fakeHttpErrorResponse()
        Mockito.`when`(mockMoviesEntryPoint.requestPopularMovies(page = 1))
            .thenAnswer { error }
        val expected = PagingSource.LoadResult.Error<Int, MoviesResponse>(HttpException(error))
        val actual = moviesPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )
        assertThat(expected::class).isEqualTo(actual::class)
    }

    @Test
    fun `videos paging source append last page is success test`() = runBlockingTest {
        mockVideos.add(fakeCorrectMoviesResponse(2, 2))

        Mockito.`when`(mockMoviesEntryPoint.requestPopularMovies(page = 2))
            .thenAnswer { mockVideos[0] }

        val actual = moviesPagingSource.load(
            PagingSource.LoadParams.Append(
                key = 2,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )
        val expected = PagingSource.LoadResult.Page(
            data = listOf(mockVideos[0]).flatMap { it.body()!!.moviesList },
            prevKey = 1,
            nextKey = null,
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `videos paging source prepend first page is success test`() = runBlockingTest {
        mockVideos.add(fakeCorrectMoviesResponse(1, 2))

        Mockito.`when`(mockMoviesEntryPoint.requestPopularMovies(page = 1))
            .thenAnswer { mockVideos[0] }

        val actual = moviesPagingSource.load(
            PagingSource.LoadParams.Prepend(
                key = 1,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )
        val expected = PagingSource.LoadResult.Page(
            data = listOf(mockVideos[0]).flatMap { it.body()!!.moviesList },
            prevKey = null,
            nextKey = 2,
        )

        assertThat(actual).isEqualTo(expected)
    }


}