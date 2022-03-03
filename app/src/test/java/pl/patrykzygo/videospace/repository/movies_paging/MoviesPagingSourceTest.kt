package pl.patrykzygo.videospace.repository.movies_paging

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import pl.patrykzygo.videospace.data.network.MovieResponse
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import pl.patrykzygo.videospace.util.fakeCorrectMoviesResponse
import pl.patrykzygo.videospace.util.fakeHttpErrorResponse
import retrofit2.HttpException

@ExperimentalCoroutinesApi
class MoviesPagingSourceTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()


    @Mock
    private lateinit var mockMoviesEntryPoint: MoviesEntryPoint

    private lateinit var moviesPagingSourceImpl: MoviesPagingSourceImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        moviesPagingSourceImpl = MoviesPagingSourceImpl(mockMoviesEntryPoint)
    }


    @Test
    fun `videos paging source refresh is success test`() = runTest {
        val fakeMoviesResponse = fakeCorrectMoviesResponse(1, 2)

        Mockito.`when`(mockMoviesEntryPoint.requestPopularMovies(page = 1))
            .thenAnswer { fakeMoviesResponse }

        val actual = moviesPagingSourceImpl.load(
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
    fun `movies paging source load is http error`() = runTest {
        val error = fakeHttpErrorResponse()
        Mockito.`when`(mockMoviesEntryPoint.requestPopularMovies(page = 1))
            .thenAnswer { error }
        val expected = PagingSource.LoadResult.Error<Int, MovieResponse>(HttpException(error))
        val actual = moviesPagingSourceImpl.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )
        assertThat(actual::class).isEqualTo(expected::class)
    }

    @Test
    fun `videos paging source append last page is success test`() = runTest {
        val fakeResponse = fakeCorrectMoviesResponse(2, 2)

        Mockito.`when`(mockMoviesEntryPoint.requestPopularMovies(page = 2))
            .thenAnswer { fakeResponse }

        val actual = moviesPagingSourceImpl.load(
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
    fun `videos paging source prepend first page is success test`() = runTest {
        val fakeResponse = fakeCorrectMoviesResponse(1, 2)

        Mockito.`when`(mockMoviesEntryPoint.requestPopularMovies(page = 1))
            .thenAnswer { fakeResponse }

        val actual = moviesPagingSourceImpl.load(
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