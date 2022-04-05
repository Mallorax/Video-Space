package pl.patrykzygo.videospace.ui

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.TestFragmentFactory
import pl.patrykzygo.videospace.constants.MoviesRequestType
import pl.patrykzygo.videospace.util.launchFragmentInHiltContainer
import pl.patrykzygo.videospace.util.provideMovieWithIdUi
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class DefaultListsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()


    @Inject
    lateinit var testFragmentFactory: TestFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }


    @Test
    fun testNavigationToDetailsView() {
        val movie = provideMovieWithIdUi(1)
        launchFragmentInHiltContainer<HomeFragment>(fragmentFactory = testFragmentFactory) {
            val bundle = Bundle()
            bundle.putParcelable("movie", movie)
            parentFragmentManager.setFragmentResult("movieResult", bundle)
        }

        assertThat(testFragmentFactory.navController.currentDestination?.id).isEqualTo(R.id.movie_details)

    }

    @Test
    fun testPutFragmentInContainer() {
        val expectedRequestType = MoviesRequestType.SIMILAR
        var bundle: Bundle? = null
        val testFragment = Fragment()
        launchFragmentInHiltContainer<HomeFragment>(fragmentFactory = testFragmentFactory) {
            addFragmentToContainer(
                binding.mostPopularMoviesContainer.id,
                expectedRequestType,
                testFragment
            )
            bundle = testFragment.arguments
        }
        val resultingRequestType = bundle?.getString("request_type")
        assertThat(resultingRequestType).isEqualTo(expectedRequestType)

    }
}