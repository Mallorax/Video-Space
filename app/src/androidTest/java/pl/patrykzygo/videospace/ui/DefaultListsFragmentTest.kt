package pl.patrykzygo.videospace.ui

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.UICoroutineRule
import pl.patrykzygo.videospace.launchFragmentInHiltContainer
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
    fun teardown() {
        testFragmentFactory = null
    }

    @Test
    fun testNavigationToDetailsView() {
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