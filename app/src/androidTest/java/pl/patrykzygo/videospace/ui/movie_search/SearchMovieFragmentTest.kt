package pl.patrykzygo.videospace.ui.movie_search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.TestFragmentFactory
import pl.patrykzygo.videospace.di.RetrofitModule
import pl.patrykzygo.videospace.ui.movie_details.MovieDetailsFragment
import pl.patrykzygo.videospace.util.launchFragmentInHiltContainer
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@UninstallModules(RetrofitModule::class)
class SearchMovieFragmentTest() {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var testFragmentFactory: TestFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun testNavigateToMoviesList(){
        launchFragmentInHiltContainer<SearchMovieFragment>(fragmentFactory = testFragmentFactory) {
            viewModel.submitRequest(1, "234")
        }
        assertThat(testFragmentFactory.navController.currentDestination?.id).isEqualTo(R.id.movies_list_fragment)
    }


}