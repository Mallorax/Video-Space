package pl.patrykzygo.videospace.ui.movies_gallery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
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
import pl.patrykzygo.videospace.util.clickChildWithId
import pl.patrykzygo.videospace.util.launchFragmentInHiltContainer
import pl.patrykzygo.videospace.util.provideMovieWithIdUi
import javax.inject.Inject


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class MoviesGalleryFragmentTest {

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
    fun testIsLabelSetProperly() {
        var given: String? = null
        val expected = "test"
        launchFragmentInHiltContainer<MoviesGalleryFragment>(fragmentFactory = testFragmentFactory) {
            viewModel.setRequestType(expected)
            given = binding.listLabel.text.toString()
        }
        assertThat(given).isEqualTo(expected)
    }

    @Test
    fun clickMovieListItem_showBottomSheetDialog() {
        launchFragmentInHiltContainer<MoviesGalleryFragment>(fragmentFactory = testFragmentFactory) {
            val movies = PagingData.from(listOf(provideMovieWithIdUi(1), provideMovieWithIdUi(2)))
            adapter.submitData(lifecycle, movies)
            this.binding.moviesListRecycler.adapter = adapter
        }
        onView(withId(R.id.movies_list_recycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MoviesGalleryRecyclerAdapter.MovieItemViewHolder>(
                0,
                clickChildWithId(R.id.image_view_more)
            )
        )
        onView(withId(R.id.modal_bottom_sheet_root_layout)).inRoot(RootMatchers.isFocusable())
            .check(
                matches(isDisplayed())
            )

    }

}