package pl.patrykzygo.videospace.ui.movie_details

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.patrykzygo.videospace.AndroidMainDispatcherRule
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.TestFragmentFactory
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.di.RetrofitModule
import pl.patrykzygo.videospace.others.MoviesRequestType
import pl.patrykzygo.videospace.ui.binding_adapters.getAverageVoteString
import pl.patrykzygo.videospace.ui.binding_adapters.getVoteCountString
import pl.patrykzygo.videospace.util.launchFragmentInHiltContainer
import pl.patrykzygo.videospace.util.provideMovieWithIdUi
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@UninstallModules(RetrofitModule::class)
class MovieDetailsFragmentTest() {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var testFragmentFactory: TestFragmentFactory
    lateinit var expectedMovie: Movie


    @Before
    fun setup() {
        hiltRule.inject()
        expectedMovie = provideMovieWithIdUi(1)
    }

    @Test
    fun testPutFragmentInContainer() {
        val expectedRequestType = MoviesRequestType.SIMILAR
        var bundle: Bundle? = null
        val testFragment = Fragment()
        launchFragmentInHiltContainer<MovieDetailsFragment>(fragmentFactory = testFragmentFactory) {
            movieId = expectedMovie.id
            addMoviesListFragmentToContainer(
                binding.similarMoviesContainer.id,
                expectedRequestType, testFragment
            )
            bundle = testFragment.arguments
        }
        val resultingRequestType = bundle?.getString("request_type")
        val resultingId = bundle?.getInt("movieId")
        assertThat(resultingRequestType).isEqualTo(expectedRequestType)
        assertThat(resultingId).isEqualTo(expectedMovie.id)
    }

    @Test
    fun testNavigationToSelf() {
        val movie = provideMovieWithIdUi(1)
        var resultedMovieId: Int? = null
        testFragmentFactory.navController.addOnDestinationChangedListener { _, _, arguments ->
            resultedMovieId = arguments?.let { MovieDetailsFragmentArgs.fromBundle(it).movieId }
        }
        launchFragmentInHiltContainer<MovieDetailsFragment>(fragmentFactory = testFragmentFactory) {
            movieId = movie.id
            val bundle = Bundle()
            bundle.putParcelable("movie", movie)
            parentFragmentManager.setFragmentResult("movieResult", bundle)
        }
        assertThat(resultedMovieId).isEqualTo(movie.id)
    }

    @Test
    fun testNavigationToSearchFragment() {
        launchFragmentInHiltContainer<MovieDetailsFragment>(fragmentFactory = testFragmentFactory) {
            viewModel.setMovie(1)
        }
        onView(withId(R.id.details_fab)).perform(click())
        assertThat(testFragmentFactory.navController.currentDestination?.id).isEqualTo(R.id.save_movie_fragment)
    }

    @Test
    fun testNavigationToListFragment(){
        launchFragmentInHiltContainer<MovieDetailsFragment>(fragmentFactory = testFragmentFactory) {
            viewModel.moveToGenreList("1")
        }
        assertThat(testFragmentFactory.navController.currentDestination?.id).isEqualTo(R.id.movies_list_fragment)
    }


    //view binding tests
    @Test
    fun testMovieIsTitleSet() {
        launchFragmentInHiltContainer<MovieDetailsFragment>(fragmentFactory = testFragmentFactory) {
            movieId = expectedMovie.id
            viewModel.setMovie(expectedMovie.id)
        }
        onView(withId(R.id.movie_title)).check(matches(withText(expectedMovie.title)))
    }

    @Test
    fun testIsMovieDescriptionSet() {
        launchFragmentInHiltContainer<MovieDetailsFragment>(fragmentFactory = testFragmentFactory) {
            movieId = expectedMovie.id
            viewModel.setMovie(expectedMovie.id)
        }
        onView(withId(R.id.movie_description)).check(matches(withText(containsString(expectedMovie.overview))))
    }

    @Test
    fun testIsMovieRatingSet() {
        val movie = provideMovieWithIdUi(1)
        launchFragmentInHiltContainer<MovieDetailsFragment>(fragmentFactory = testFragmentFactory) {
            movieId = movie.id
            viewModel.setMovie(movie.id)
        }
        val expectedAverageVote = getAverageVoteString(movie.voteAverage)
        onView(withId(R.id.movie_rating)).check(matches(withText(containsString(expectedAverageVote))))
    }

    @Test
    fun testIsVoteCountSet() {
        val movie = provideMovieWithIdUi(1)
        launchFragmentInHiltContainer<MovieDetailsFragment>(fragmentFactory = testFragmentFactory) {
            movieId = movie.id
            viewModel.setMovie(movie.id)
        }
        val expectedVoteCount = getVoteCountString(movie.voteCount)
        onView(withId(R.id.movie_vote_count)).check(
            matches(
                withText(
                    expectedVoteCount
                )
            )
        )
    }


}