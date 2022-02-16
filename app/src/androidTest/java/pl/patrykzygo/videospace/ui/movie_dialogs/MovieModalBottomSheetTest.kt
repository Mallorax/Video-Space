package pl.patrykzygo.videospace.ui.movie_dialogs

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.patrykzygo.videospace.FakeLocalStoreRepositoryAndroid
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.UICoroutineRule
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.getOrAwaitValueTestAndroid
import pl.patrykzygo.videospace.util.launchFragmentInHiltContainer
import pl.patrykzygo.videospace.util.provideMovieWithIdUi

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

    lateinit var viewModel: MovieBottomSheetViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = MovieBottomSheetViewModel(FakeLocalStoreRepositoryAndroid())
    }

    @Test
    fun testIfFavouriteImageWasChangedOnClick() {
        val movie = provideMovieWithIdUi(1)

        launchFragmentInHiltContainer<MovieModalBottomSheet> {
            viewModel = this@MovieModalBottomSheetTest.viewModel
            viewModel.setMovie(movie)
        }
        onView(withId(R.id.like_text_view)).perform(click())

    }

    @Test
    fun testSetMovieToViewModel() = runBlockingTest {
        val movie = provideMovieWithIdUi(1)
        launchFragmentInHiltContainer<MovieModalBottomSheet> {
            this.viewModel = this@MovieModalBottomSheetTest.viewModel
            viewModel.setMovie(movie)
        }

        val value = viewModel.movie.getOrAwaitValueTestAndroid()
        assertThat(value).isEqualTo(movie)
    }

    @Test
    fun testIfFragmentResultIsSet() {
        val expectedMovie = provideMovieWithIdUi(1)
        var resultedMovie: Movie? = null
        launchFragmentInHiltContainer<MovieModalBottomSheet> {
            this.viewModel = this@MovieModalBottomSheetTest.viewModel
            viewModel.setMovie(expectedMovie)
            parentFragmentManager.setFragmentResultListener("movieResult", this) { _, bundle ->
                resultedMovie = bundle.getParcelable("movie")
            }
        }
        onView(withId(R.id.more_info_text_view)).perform(click())
        assertThat(resultedMovie).isEqualTo(expectedMovie)
    }


}