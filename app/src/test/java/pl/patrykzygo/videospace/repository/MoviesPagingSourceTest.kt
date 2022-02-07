package pl.patrykzygo.videospace.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import pl.patrykzygo.videospace.data.network.MovieResponse
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import pl.patrykzygo.videospace.fakeCorrectMoviesResponse
import pl.patrykzygo.videospace.fakeHttpErrorResponse
import retrofit2.HttpException

@ExperimentalCoroutinesApi
class MoviesPagingSourceTest {

    private lateinit var moviesPagingSource: MoviesPagingSource

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockMoviesEntryPoint: MoviesEntryPoint

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        moviesPagingSource = MoviesPagingSource(mockMoviesEntryPoint)
    }


    @Test
    fun `videos paging source refresh is success test`() = runBlockingTest {
        val fakeMoviesResponse = fakeCorrectMoviesResponse(1, 2)

        Mockito.`when`(mockMoviesEntryPoint.requestPopularMovies(page = 1))
            .thenAnswer { fakeMoviesResponse }

        val actual = moviesPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        val expected = PagingSource.LoadResult.Page(
            data = listOf(fakeMoviesResponse).flatMap { it.body()!!.movieList },
            prevKey = null,
            nextKey = fakeMoviesResponse.body()?.page?.plus(1),
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `movies paging source load is http error`() = runBlockingTest {
        val error = fakeHttpErrorResponse()
        Mockito.`when`(mockMoviesEntryPoint.requestPopularMovies(page = 1))
            .thenAnswer { error }
        val expected = PagingSource.LoadResult.Error<Int, MovieResponse>(HttpException(error))
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
        val fakeResponse = fakeCorrectMoviesResponse(2, 2)

        Mockito.`when`(mockMoviesEntryPoint.requestPopularMovies(page = 2))
            .thenAnswer { fakeResponse }

        val actual = moviesPagingSource.load(
            PagingSource.LoadParams.Append(
                key = 2,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )
        val expected = PagingSource.LoadResult.Page(
            data = listOf(fakeResponse).flatMap { it.body()!!.movieList },
            prevKey = 1,
            nextKey = null,
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `videos paging source prepend first page is success test`() = runBlockingTest {
        val fakeResponse = fakeCorrectMoviesResponse(1, 2)

        Mockito.`when`(mockMoviesEntryPoint.requestPopularMovies(page = 1))
            .thenAnswer { fakeResponse }

        val actual = moviesPagingSource.load(
            PagingSource.LoadParams.Prepend(
                key = 1,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )
        val expected = PagingSource.LoadResult.Page(
            data = listOf(fakeResponse).flatMap { it.body()!!.movieList },
            prevKey = null,
            nextKey = 2,
        )

        assertThat(actual).isEqualTo(expected)
    }


}