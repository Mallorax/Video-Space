package pl.patrykzygo.videospace.ui.stored_list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.patrykzygo.MainDispatcherRule
import pl.patrykzygo.videospace.TestDispatcherProvider
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.others.MovieStatus
import pl.patrykzygo.videospace.repository.FakeLocalStoreMoviesRepository
import pl.patrykzygo.videospace.util.getOrAwaitValueTest

@ExperimentalCoroutinesApi
class StoredListViewModelTest {

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: StoredListViewModel
    private var fakeMoviesRepo = FakeLocalStoreMoviesRepository()

    @Before
    fun setup() {
        viewModel = StoredListViewModel(
            fakeMoviesRepo,
            TestDispatcherProvider()
        )
    }

    @Test
    fun `getMoviesWithStatus shows error when status is wrong`() {
        val expected = StoredListViewModel.WRONG_STATUS_ERROR
        viewModel.getMoviesWithStatus("")
        val actual = viewModel.errorMessage.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `getMoviesWithStatus handle error from repo`() {
        val expected = FakeLocalStoreMoviesRepository.MOVIES_WITH_STATUS_ERROR
        //Fake repo returns error with DROPPED status for testing purposes
        viewModel.getMoviesWithStatus(MovieStatus.DROPPED)
        val actual = viewModel.errorMessage.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `getMoviesWithStatus returns success`() {
        val expected = mutableListOf<MovieEntity>()
        for (i in 10..15) {
            val movie = MovieEntity(i, i.toString(), status = MovieStatus.PLAN_TO_WATCH)
            fakeMoviesRepo.movieList.add(movie)
            expected.add(movie)
        }
        viewModel.getMoviesWithStatus(MovieStatus.PLAN_TO_WATCH)
        val result = viewModel.movies.getOrAwaitValueTest(5)
        val isResultTheSame = checkListsEqualityWithId(result, expected)
        assertThat(isResultTheSame).isTrue()
    }

    private fun checkListsEqualityWithId(
        actual: List<Movie>,
        expected: List<MovieEntity>
    ): Boolean {
        actual.forEach { actualMovie ->
            if (!expected.any { expectedMovie -> expectedMovie.movieId == actualMovie.id }) return false
        }
        return true
    }
}