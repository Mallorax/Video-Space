package pl.patrykzygo.videospace.ui.movie_details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.UICoroutineRule
import pl.patrykzygo.videospace.ui.TestFragmentFactory
import pl.patrykzygo.videospace.util.launchFragmentInHiltContainer
import pl.patrykzygo.videospace.util.provideMovieWithIdUi
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class MovieDetailsFragmentTest(){

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = UICoroutineRule()

    @Inject
    lateinit var testNavController: NavController

    lateinit var viewModel: MovieDetailsViewModel

    var testFragmentFactory: TestFragmentFactory? = null

    @Before
    fun setup(){
        hiltRule.inject()
        viewModel = MovieDetailsViewModel()
        testFragmentFactory = TestFragmentFactory(testNavController)
    }

    @Test
    fun testIsMovieSet(){
        val movie = provideMovieWithIdUi(1)
        launchFragmentInHiltContainer<MovieDetailsFragment>(fragmentFactory = testFragmentFactory) {
            this.movie = movie
            viewModel = this@MovieDetailsFragmentTest.viewModel
            viewModel.setMovie(movie)
        }
        onView(withId(R.id.poster_image_view)).perform(click())
        onView(withId(R.id.poster_image_view)).check(matches(isDisplayed()))
    }
}