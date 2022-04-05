package pl.patrykzygo.videospace.factories

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import pl.patrykzygo.videospace.ui.HomeFragment
import pl.patrykzygo.videospace.ui.movie_details.MovieDetailsFragment
import pl.patrykzygo.videospace.ui.movie_dialogs.MovieModalBottomSheet
import pl.patrykzygo.videospace.ui.movie_search.SearchMovieFragment
import pl.patrykzygo.videospace.ui.movies_gallery.MoviesGalleryFragment
import pl.patrykzygo.videospace.ui.movies_list.MoviesListFragment
import pl.patrykzygo.videospace.ui.save_movie.SaveMovieFragment
import pl.patrykzygo.videospace.ui.user_lists.UserListsFragment
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DefaultFragmentFactory
@Inject constructor(
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            HomeFragment::class.java.name -> HomeFragment()
            MoviesListFragment::class.java.name -> MoviesListFragment()
            MoviesGalleryFragment::class.java.name -> MoviesGalleryFragment()
            MovieModalBottomSheet::class.java.name -> MovieModalBottomSheet()
            MovieDetailsFragment::class.java.name -> MovieDetailsFragment()
            SaveMovieFragment::class.java.name -> SaveMovieFragment()
            SearchMovieFragment::class.java.name -> SearchMovieFragment()
            UserListsFragment::class.java.name -> UserListsFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}