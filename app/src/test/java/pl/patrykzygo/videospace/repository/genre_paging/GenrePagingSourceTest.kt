package pl.patrykzygo.videospace.repository.genre_paging

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
import pl.patrykzygo.videospace.networking.DiscoverEntryPoint
import pl.patrykzygo.videospace.util.fakeCorrectMoviesResponse
import pl.patrykzygo.videospace.util.fakeHttpErrorResponse
import retrofit2.HttpException

@ExperimentalCoroutinesApi
class GenrePagingSourceTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockDiscoverEntryPoint: DiscoverEntryPoint
    private lateinit var genrePagingSourceImpl: GenrePagingSourceImpl
    private val genre = 5

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        genrePagingSourceImpl = GenrePagingSourceImpl(mockDiscoverEntryPoint)
        genrePagingSourceImpl.setParameters(genre)
    }

    @Test
    fun `genre videos paging source refresh is  success test`() = runTest {
        val page = 1
        val fakeResponse = fakeCorrectMoviesResponse(page, 2)

        Mockito.`when`(
            mockDiscoverEntryPoint.getMoviesWithGenre(
                page = page,
                includedGenres = genre.toString()
            )
        )
            .thenAnswer { fakeResponse }

        val actual = genrePagingSourceImpl.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = true
            )
        )

        val expected = PagingSource.LoadResult.Page(
            data = listOf(fakeResponse).flatMap { it.body()!!.movieList },
            prevKey = null,
            nextKey = fakeResponse.body()?.page?.plus(1)
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `genre videos source load is http error`() = runTest {
        val error = fakeHttpErrorResponse()
        Mockito.`when`(
            mockDiscoverEntryPoint.getMoviesWithGenre(
                page = 1,
                includedGenres = genre.toString()
            )
        ).thenAnswer { error }
        val expected = PagingSource.LoadResult.Error<Int, MovieResponse>(HttpException(error))
        val actual = genrePagingSourceImpl.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )
        assertThat(actual::class).isEqualTo(expected::class)
    }

    @Test
    fun `genres paging source append last page is success test`() = runTest {
        val page = 2
        val fakeResponse = fakeCorrectMoviesResponse(page, 2)
        Mockito.`when`(
            mockDiscoverEntryPoint.getMoviesWithGenre(
                includedGenres = genre.toString(),
                page = page
            )
        ).thenAnswer { fakeResponse }

        val actual = genrePagingSourceImpl.load(
            PagingSource.LoadParams.Append(
                key = page,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        val expected = PagingSource.LoadResult.Page(
            data = listOf(fakeResponse).flatMap { it.body()!!.movieList },
            prevKey = 1,
            nextKey = null
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `genres paging source prepend page is success test`() = runTest {
        val page = 1
        val fakeResponse = fakeCorrectMoviesResponse(page, 2)
        Mockito.`when`(
            mockDiscoverEntryPoint.getMoviesWithGenre(
                includedGenres = genre.toString(),
                page = page
            )
        ).thenAnswer { fakeResponse }

        val actual = genrePagingSourceImpl.load(
            PagingSource.LoadParams.Prepend(
                key = 1,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        val expected = PagingSource.LoadResult.Page(
            data = listOf(fakeResponse).flatMap { it.body()!!.movieList },
            prevKey = null,
            nextKey = 2
        )

        assertThat(actual).isEqualTo(expected)
    }
}