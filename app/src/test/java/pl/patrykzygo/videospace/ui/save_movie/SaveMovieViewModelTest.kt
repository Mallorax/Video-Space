package pl.patrykzygo.videospace.ui.save_movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.patrykzygo.MainDispatcherRule
import pl.patrykzygo.videospace.TestDispatcherProvider
import pl.patrykzygo.videospace.constants.MovieStatus
import pl.patrykzygo.videospace.repository.FakeLocalStoreMoviesRepository
import pl.patrykzygo.videospace.util.getOrAwaitValueTest

@ExperimentalCoroutinesApi
class SaveMovieViewModelTest {

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var dispatcherRule = MainDispatcherRule()

    lateinit var viewModel: SaveMovieViewModel
    var moviesRepo = FakeLocalStoreMoviesRepository()

    @Before
    fun setup() {
        viewModel = SaveMovieViewModel(moviesRepo, TestDispatcherProvider())
    }

    @Test
    fun `saveMovie title is null shows error message`() {
        val expected = SaveMovieViewModel.SAVE_WRONG_UNRECOGNISABLE_MOVIE
        viewModel.selectStatus(MovieStatus.PLAN_TO_WATCH)
        viewModel.saveMovie(1, null, 2)
        val actual = viewModel.inputFeedbackMessage.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `saveMovie id is null shows error message`() {
        val expected = SaveMovieViewModel.SAVE_WRONG_UNRECOGNISABLE_MOVIE
        viewModel.selectStatus(MovieStatus.PLAN_TO_WATCH)
        viewModel.saveMovie(null, "test", 2)
        val actual = viewModel.inputFeedbackMessage.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `saveMovie status is null shows error message`() {
        val expected = SaveMovieViewModel.SAVE_WRONG_STATUS
        viewModel.selectStatus(MovieStatus.UNASSIGNED)
        viewModel.saveMovie(1, "test",  2)
        val actual = viewModel.inputFeedbackMessage.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `saveMovie status is incorrect shows error message`() {
        val expected = SaveMovieViewModel.SAVE_WRONG_STATUS
        viewModel.selectStatus("wrong status")
        viewModel.saveMovie(1, "test", 2)
        val actual = viewModel.inputFeedbackMessage.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `saveMovie score is incorrect shows error message`() {
        val expected = SaveMovieViewModel.SAVE_WRONG_INCORRECT_SCORE
        viewModel.selectStatus(MovieStatus.PLAN_TO_WATCH)
        viewModel.saveMovie(1, "test", -1)
        val actual = viewModel.inputFeedbackMessage.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `saveMovie movie is saved correctly`() {
        val expected = SaveMovieViewModel.SAVE_SUCCESSFUL_MSG
        viewModel.selectStatus(MovieStatus.PLAN_TO_WATCH)
        viewModel.saveMovie(1, "test", 2)
        val actual = viewModel.inputFeedbackMessage.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(expected)
    }
}