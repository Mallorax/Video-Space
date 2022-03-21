package pl.patrykzygo.videospace.ui.movie_details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.patrykzygo.MainDispatcherRule
import pl.patrykzygo.videospace.TestDispatcherProvider
import pl.patrykzygo.videospace.data.app.DiscoverMovieRequest
import pl.patrykzygo.videospace.repository.FakeLocalStoreGenresRepository
import pl.patrykzygo.videospace.repository.FakeLocalStoreMoviesRepository
import pl.patrykzygo.videospace.util.fakeGenreList
import pl.patrykzygo.videospace.util.getMovieWithId
import pl.patrykzygo.videospace.util.getOrAwaitValueTest

@ExperimentalCoroutinesApi
class MovieDetailsViewModelTest {

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: MovieDetailsViewModel

    private val moviesRepo = FakeLocalStoreMoviesRepository()
    private val genreRepo = FakeLocalStoreGenresRepository(fakeGenreList().toMutableList())

    @Before
    fun setup() {
        viewModel = MovieDetailsViewModel(
            moviesRepo,
            genreRepo,
            TestDispatcherProvider()
        )
    }


    @Test
    fun `setMovie shows correct genres`() {
        val movie = getMovieWithId(1)
        val expected = movie.genres
        var result: List<String>? = null
        viewModel.genres.observeForever {
            result = it
        }
        viewModel.setMovie(movie.id)
        assertThat(result).containsExactlyElementsIn(expected)
    }

    @Test
    fun `setMovie shows correctMovie`() {
        val expectedMovie = getMovieWithId(1)
        viewModel.setMovie(expectedMovie.id)
        val actual = viewModel.movie.getOrAwaitValueTest()
        assertThat(expectedMovie.id).isEqualTo(actual?.id)
    }

    @Test
    fun `setMovie on response failure error is message is set`() {
        val expected = "test error"
        viewModel.setMovie(-1)
        val result = viewModel.errorMessage.getOrAwaitValueTest()
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `on moveToGenreList event is fired with success`() {
        var actualRequest: DiscoverMovieRequest? = null
        viewModel.searchInGenreLiveEvent.observeForever {
            actualRequest = it
        }
        val expected = "2"
        viewModel.moveToGenreList("2")
        assertThat(actualRequest?.includedGenres).isEqualTo(expected)
    }

    @Test
    fun `on moveToGenreList event is failure with error message`() {
        val expected = "Check your internet connection"
        genreRepo.genres = mutableListOf()
        viewModel.moveToGenreList("1")
        val actual = viewModel.searchInGenreErrorMessage.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `check if saveMovieEvent was fired`(){
        var wasFired = false
        viewModel.saveMovieEvent.observeForever{wasFired = true}
        viewModel.launchSaveMovieEvent()
        assertThat(wasFired).isTrue()
    }
}