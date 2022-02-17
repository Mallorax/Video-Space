package pl.patrykzygo.videospace.ui.movie_details

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.containsString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.UICoroutineRule
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.ui.TestFragmentFactory
import pl.patrykzygo.videospace.ui.movies_list.getAverageVoteString
import pl.patrykzygo.videospace.ui.movies_list.getVoteCountString
import pl.patrykzygo.videospace.util.launchFragmentInHiltContainer
import pl.patrykzygo.videospace.util.provideMovieWithIdUi
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class MovieDetailsFragmentTest() {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = UICoroutineRule()

    @Inject
    lateinit var testNavController: TestNavHostController

    lateinit var viewModel: MovieDetailsViewModel

    var testFragmentFactory: TestFragmentFactory? = null

    @Before
    fun setup() {
        hiltRule.inject()
        testNavController.setCurrentDestination(R.id.movie_details)
        viewModel = MovieDetailsViewModel()
        testFragmentFactory = TestFragmentFactory(testNavController)
    }


    @Test
    fun testNavigationToSelf(){
        val movie = provideMovieWithIdUi(1)
        var resultedMovie: Movie? = null
        testNavController.addOnDestinationChangedListener { controller, destination, arguments ->
            resultedMovie = arguments?.getParcelable("movie")
        }
        launchFragmentInHiltContainer<MovieDetailsFragment>(fragmentFactory = testFragmentFactory) {
            this.movie = movie
            val bundle = Bundle()
            bundle.putParcelable("movie", movie)
            parentFragmentManager.setFragmentResult("movieResult", bundle)
        }
        assertThat(resultedMovie).isEqualTo(movie)
    }

    //view binding tests
    @Test
    fun testMovieIsTitleSet() {
        val movie = provideMovieWithIdUi(1)
        launchFragmentInHiltContainer<MovieDetailsFragment>(fragmentFactory = testFragmentFactory) {
            this.movie = movie
            viewModel.setMovie(movie)
        }
        onView(withId(R.id.movie_title)).check(matches(withText(containsString(movie.title))))
    }

    @Test
    fun testIsMovieDescriptionSet() {
        val movie = provideMovieWithIdUi(1)
        launchFragmentInHiltContainer<MovieDetailsFragment>(fragmentFactory = testFragmentFactory) {
            this.movie = movie
            viewModel.setMovie(movie)
        }
        onView(withId(R.id.movie_description)).check(matches(withText(containsString(movie.overview))))
    }

    @Test
    fun testIsMovieRatingSet() {
        val movie = provideMovieWithIdUi(1)
        launchFragmentInHiltContainer<MovieDetailsFragment>(fragmentFactory = testFragmentFactory) {
            this.movie = movie
            viewModel.setMovie(movie)
        }
        val expectedAverageVote = getAverageVoteString(movie.voteAverage)
        onView(withId(R.id.movie_rating)).check(matches(withText(containsString(expectedAverageVote))))
    }

    @Test
    fun testIsVoteCountSet() {
        val movie = provideMovieWithIdUi(1)
        launchFragmentInHiltContainer<MovieDetailsFragment>(fragmentFactory = testFragmentFactory) {
            this.movie = movie
            viewModel.setMovie(movie)
        }
        val expectedVoteCount = getVoteCountString(movie.voteCount)
        onView(withId(R.id.movie_vote_count)).check(matches(withText(containsString(expectedVoteCount))))
    }
}