package pl.patrykzygo.videospace.ui.movies_list

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.UICoroutineRule
import pl.patrykzygo.videospace.launchFragmentInHiltContainer
import pl.patrykzygo.videospace.provideMovieWithIdUi


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class MoviesListFragmentTest {

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
    fun clickMovieListItem_showBottomSheetDialog() {
        launchFragmentInHiltContainer<MoviesListFragment> {
            val movies = PagingData.from(listOf(provideMovieWithIdUi(1), provideMovieWithIdUi(2)))
            runBlockingTest { adapter.submitData(movies) }
            this.binding.moviesListRecycler.adapter = adapter
        }
        onView(withId(R.id.movies_list_recycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MoviesListRecyclerAdapter.MovieItemViewHolder>(
                0,
                clickChildWithId(R.id.image_view_more)
            )
        )
        onView(withId(R.id.modal_bottom_sheet_root_layout)).inRoot(RootMatchers.isFocusable())
            .check(
                matches(isDisplayed())
            )

    }

    private fun clickChildWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return withEffectiveVisibility(Visibility.VISIBLE)
            }

            override fun getDescription(): String {
                return "Description"
            }

            override fun perform(uiController: UiController?, view: View?) {
                val img = view?.findViewById<View>(id)
                img?.performClick()
            }
        }

    }
}