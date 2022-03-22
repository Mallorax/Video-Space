package pl.patrykzygo.videospace.ui.movies_list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import pl.patrykzygo.MainDispatcherRule

@ExperimentalCoroutinesApi
class MoviesListViewModelTest{

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: MoviesListViewModel
}