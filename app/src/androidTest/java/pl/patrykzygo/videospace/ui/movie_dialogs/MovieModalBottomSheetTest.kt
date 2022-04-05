package pl.patrykzygo.videospace.ui.movie_dialogs

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
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
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.app.SimpleMovie
import pl.patrykzygo.videospace.ui.movies_gallery.MoviesGalleryFragment
import pl.patrykzygo.videospace.ui.view_holders.GalleryItemViewHolder
import pl.patrykzygo.videospace.util.launchFragmentInHiltContainer
import pl.patrykzygo.videospace.util.provideMovieWithIdUi
import pl.patrykzygo.videospace.util.provideSimpleMovieWithIdUi
import javax.inject.Inject


//When launching this fragment by itself espresso wouldn't be able to get access to it's views.
//To handle this, in tests that require view ids, fragment is being launched from movie list fragment
@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class MovieModalBottomSheetTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()


    @Inject
    lateinit var fragmentFactory: TestFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }


    @Test
    fun testIfFragmentResultIsSet() {
        val expectedMovieId = provideMovieWithIdUi(1).id
        var resultedMovieId: Int = -1
        launchFragmentInHiltContainer<MoviesGalleryFragment>(fragmentFactory = fragmentFactory) {
            val movies = PagingData.from(listOf(provideSimpleMovieWithIdUi(1), provideSimpleMovieWithIdUi(2)))
            adapter.submitData(lifecycle, movies)
            binding.moviesListRecycler.adapter = adapter
            binding.moviesListRecycler.visibility = View.VISIBLE
            parentFragmentManager.setFragmentResultListener("movieResult", this) { _, bundle ->
                resultedMovieId = bundle.getInt("movie")
            }

        }
        onView(withId(R.id.movies_list_recycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<GalleryItemViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.more_info_text_view)).inRoot(RootMatchers.isFocusable())
            .perform(click())
        assertThat(resultedMovieId).isEqualTo(expectedMovieId)
    }


}