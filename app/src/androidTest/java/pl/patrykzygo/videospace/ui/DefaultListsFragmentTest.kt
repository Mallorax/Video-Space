package pl.patrykzygo.videospace.ui

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.whenStarted
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.testing.TestNavHostController
import androidx.paging.PagingData
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import pl.patrykzygo.videospace.*
import pl.patrykzygo.videospace.ui.movie_dialogs.MovieModalBottomSheet
import pl.patrykzygo.videospace.ui.movies_list.MoviesListFragment
import pl.patrykzygo.videospace.ui.movies_list.MoviesListRecyclerAdapter
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class DefaultListsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = UICoroutineRule()

    @Inject
    lateinit var testNavController: NavController

    var testFragmentFactory: TestFragmentFactory? = null

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun teardown(){
        testFragmentFactory = null
    }

    @Test
    fun testNavigationToDetailsView(){
        testFragmentFactory = TestFragmentFactory(testNavController)
        val movie = provideMovieWithIdUi(1)
        launchFragmentInHiltContainer<DefaultListsFragment>(fragmentFactory = testFragmentFactory) {
            val bundle = Bundle()
            bundle.putParcelable("movie", movie)
            parentFragmentManager.setFragmentResult("movieResult", bundle)
        }

        assertThat(testNavController.currentDestination?.id).isEqualTo(R.id.movie_details)

    }
}