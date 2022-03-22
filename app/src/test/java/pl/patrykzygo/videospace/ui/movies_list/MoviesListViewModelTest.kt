package pl.patrykzygo.videospace.ui.movies_list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.patrykzygo.MainDispatcherRule
import pl.patrykzygo.videospace.others.SortOptions
import pl.patrykzygo.videospace.repository.FakeDiscoverPagingSource
import pl.patrykzygo.videospace.util.getOrAwaitValueTest

@ExperimentalCoroutinesApi
class MoviesListViewModelTest {

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: MoviesListViewModel
    private val repo = FakeDiscoverPagingSource()

    @Before
    fun setup() {
        viewModel = MoviesListViewModel(repo)
    }

    @Test
    fun `testTriggerSortByVoteCount sets opposite order`(){
        viewModel.triggerSortByVoteCount()
        val expected = viewModel.sortOption.getOrAwaitValueTest()
        viewModel.triggerSortByVoteCount()
        viewModel.triggerSortByVoteCount()
        val actual = viewModel.sortOption.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `testTriggerSortByVoteCount sets desc when in other order`(){
        viewModel.triggerSortByVoteCount()
        val actual = viewModel.sortOption.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(SortOptions.VOTE_COUNT_DESC)
    }

    @Test
    fun `testTriggerSortByVoteCount sets asc when in desc order`(){
        viewModel.triggerSortByVoteCount()
        viewModel.triggerSortByVoteCount()
        val actual = viewModel.sortOption.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(SortOptions.VOTE_COUNT_ASC)
    }

    @Test
    fun `testTriggerSortByAverageScore sets opposite order`() {
        viewModel.triggerSortByAverageScore()
        val expected = viewModel.sortOption.getOrAwaitValueTest()
        viewModel.triggerSortByAverageScore()
        viewModel.triggerSortByAverageScore()
        val actual = viewModel.sortOption.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `testTriggerSortByAverageScore sets desc when in other order`() {
        viewModel.triggerSortByAverageScore()
        val actual = viewModel.sortOption.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(SortOptions.SCORE_AVERAGE_DESC)
    }

    @Test
    fun `testTriggerSortByAverageScore sets asc when in desc order`() {
        viewModel.triggerSortByAverageScore()
        viewModel.triggerSortByAverageScore()
        val actual = viewModel.sortOption.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(SortOptions.SCORE_AVERAGE_ASC)
    }

    @Test
    fun `testTriggerSortByReleaseDate sets opposite order`() {
        viewModel.triggerSortByReleaseDate()
        val expected = viewModel.sortOption.getOrAwaitValueTest()
        viewModel.triggerSortByReleaseDate()
        viewModel.triggerSortByReleaseDate()
        val actual = viewModel.sortOption.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `testTriggerSortByReleaseDate sets desc when in other order`() {
        viewModel.triggerSortByReleaseDate()
        val actual = viewModel.sortOption.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(SortOptions.RELEASE_DATE_DESC)
    }

    @Test
    fun `testTriggerSortByReleaseDate sets asc when in desc order`() {
        viewModel.triggerSortByReleaseDate()
        viewModel.triggerSortByReleaseDate()
        val actual = viewModel.sortOption.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(SortOptions.RELEASE_DATE_ASC)
    }

    @Test
    fun `testTriggerSortByMostPopular sets opposite order`() {
        viewModel.triggerSortByMostPopular()
        val actual = viewModel.sortOption.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(SortOptions.POPULARITY_ASC)
    }

    @Test
    fun `testTriggerSortByMostPopular sets desc order when in other order`() {
        viewModel.triggerSortByAverageScore()
        viewModel.triggerSortByMostPopular()
        val actual = viewModel.sortOption.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(SortOptions.POPULARITY_DESC)
    }

    @Test
    fun `testTriggerSortByMostPopular sets asc order when in desc order`() {
        viewModel.triggerSortByMostPopular()
        viewModel.triggerSortByMostPopular()
        val actual = viewModel.sortOption.getOrAwaitValueTest()
        assertThat(actual).isEqualTo(SortOptions.POPULARITY_DESC)
    }
}