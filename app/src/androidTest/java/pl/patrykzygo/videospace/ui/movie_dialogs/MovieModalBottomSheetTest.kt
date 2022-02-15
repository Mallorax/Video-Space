package pl.patrykzygo.videospace.ui.movie_dialogs

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.patrykzygo.videospace.*

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class MovieModalBottomSheetTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = UICoroutineRule()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testSetMovieToViewModel() = runBlockingTest {
        val viewModel = MovieBottomSheetViewModel(FakeLocalStoreRepositoryAndroid())
        val movie = provideMovieWithIdUi(1)
        launchFragmentInHiltContainer<MovieModalBottomSheet> {
            this.viewModel = viewModel
            viewModel.setMovie(movie)
        }

        val value = viewModel.movie.getOrAwaitValueTestAndroid()
        assertThat(value).isEqualTo(movie)
    }

}