package pl.patrykzygo.videospace.ui.movie_dialogs

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
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
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.patrykzygo.videospace.CustomMatchers.withDrawable
import pl.patrykzygo.videospace.FakeLocalStoreRepositoryAndroid
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.UICoroutineRule
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.di.FakeMoviePagingSourceQualifier
import pl.patrykzygo.videospace.util.getOrAwaitValueTestAndroid
import pl.patrykzygo.videospace.repository.MoviesPagingSource
import pl.patrykzygo.videospace.ui.movies_gallery.MoviesGalleryFragment
import pl.patrykzygo.videospace.ui.movies_gallery.MoviesGalleryRecyclerAdapter
import pl.patrykzygo.videospace.ui.movies_gallery.MoviesGalleryVMFactory
import pl.patrykzygo.videospace.ui.movies_gallery.MoviesGalleryViewModel
import pl.patrykzygo.videospace.util.clickChildWithId
import pl.patrykzygo.videospace.util.launchFragmentInHiltContainer
import pl.patrykzygo.videospace.util.provideMovieWithIdUi
import javax.inject.Inject
import javax.inject.Named


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

    @get:Rule
    var coroutineRule = UICoroutineRule()

    @Inject
    @FakeMoviePagingSourceQualifier
    lateinit var pagingSource: MoviesPagingSource


    lateinit var viewModel: MovieBottomSheetViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = MovieBottomSheetViewModel(FakeLocalStoreRepositoryAndroid())
    }

    //Test is sensitive to tint changes in layout's ImageView
    @Test
    fun testIfFavouriteImageWasChangedOnClick() {
        launchFragmentInHiltContainer<MoviesGalleryFragment> {
            val factory = MoviesGalleryVMFactory(pagingSource)
            viewModel = ViewModelProvider(this, factory)[MoviesGalleryViewModel::class.java]
            val movies = PagingData.from(listOf(provideMovieWithIdUi(1), provideMovieWithIdUi(2)))
            runBlockingTest { adapter.submitData(movies) }
            this.binding.moviesListRecycler.adapter = adapter
        }
        onView(withId(R.id.movies_list_recycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MoviesGalleryRecyclerAdapter.MovieItemViewHolder>(
                0,
                clickChildWithId(R.id.image_view_more)
            )
        )
        onView(withId(R.id.like_text_view)).inRoot(RootMatchers.isFocusable())
            .perform(click())
        onView(withId(R.id.favourite_image_view)).inRoot(RootMatchers.isFocusable())
            .check(matches(isDisplayed()))
            .check(
                matches(withDrawable(R.drawable.ic_baseline_favorite, tint = R.color.icon_tint))
            )

    }

    //Test is sensitive to tint changes in layout's ImageView
    @Test
    fun testIfIsOnWatchLaterImageWasChangedOnClick() {
        launchFragmentInHiltContainer<MoviesGalleryFragment> {
            val factory = MoviesGalleryVMFactory(pagingSource)
            viewModel = ViewModelProvider(this, factory)[MoviesGalleryViewModel::class.java]
            val movies = PagingData.from(listOf(provideMovieWithIdUi(1), provideMovieWithIdUi(2)))
            runBlockingTest { adapter.submitData(movies) }
            this.binding.moviesListRecycler.adapter = adapter
        }
        onView(withId(R.id.movies_list_recycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MoviesGalleryRecyclerAdapter.MovieItemViewHolder>(
                0,
                clickChildWithId(R.id.image_view_more)
            )
        )
        onView(withId(R.id.watch_later_list_text_view)).inRoot(RootMatchers.isFocusable())
            .perform(click())
        onView(withId(R.id.archive_image_view)).inRoot(RootMatchers.isFocusable())
            .check(matches(isDisplayed()))
            .check(
                matches(withDrawable(R.drawable.ic_baseline_archive, tint = R.color.icon_tint))
            )

    }

    @Test
    fun testSetMovieToViewModel() = runBlockingTest {
        val movie = provideMovieWithIdUi(1)
        launchFragmentInHiltContainer<MovieModalBottomSheet> {
            this.viewModel = this@MovieModalBottomSheetTest.viewModel
            viewModel.setMovie(movie)
        }

        val value = viewModel.movie.getOrAwaitValueTestAndroid()
        assertThat(value).isEqualTo(movie)
    }

    @Test
    fun testIfFragmentResultIsSet() {
        val expectedMovie = provideMovieWithIdUi(1)
        var resultedMovie: Movie? = null
        launchFragmentInHiltContainer<MoviesGalleryFragment> {
            val factory = MoviesGalleryVMFactory(pagingSource)
            viewModel = ViewModelProvider(this, factory)[MoviesGalleryViewModel::class.java]
            val movies = PagingData.from(listOf(provideMovieWithIdUi(1), provideMovieWithIdUi(2)))
            runBlockingTest { adapter.submitData(movies) }
            this.binding.moviesListRecycler.adapter = adapter
            parentFragmentManager.setFragmentResultListener("movieResult", this) { _, bundle ->
                resultedMovie = bundle.getParcelable("movie")
            }
        }
        onView(withId(R.id.movies_list_recycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MoviesGalleryRecyclerAdapter.MovieItemViewHolder>(
                0,
                clickChildWithId(R.id.image_view_more)
            )
        )
        onView(withId(R.id.more_info_text_view)).inRoot(RootMatchers.isFocusable())
            .perform(click())
        assertThat(resultedMovie).isEqualTo(expectedMovie)
    }


}