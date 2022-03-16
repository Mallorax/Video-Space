package pl.patrykzygo.videospace.ui.factories

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import pl.patrykzygo.videospace.ui.DefaultListsFragment
import pl.patrykzygo.videospace.ui.movie_details.MovieDetailsFragment
import pl.patrykzygo.videospace.ui.movie_dialogs.MovieModalBottomSheet
import pl.patrykzygo.videospace.ui.movie_search.SearchMovieFragment
import pl.patrykzygo.videospace.ui.movies_gallery.MoviesGalleryFragment
import pl.patrykzygo.videospace.ui.movies_list.MoviesListFragment
import pl.patrykzygo.videospace.ui.save_movie.SaveMovieFragment
import pl.patrykzygo.videospace.ui.stored_list.StoredListFragment
import pl.patrykzygo.videospace.ui.user_lists.UserListsFragment
import javax.inject.Inject
import javax.inject.Named


class DefaultFragmentFactory
@Inject constructor(
    @Named("main_vm_factory") private val viewModelFactory: MainViewModelFactory
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            DefaultListsFragment::class.java.name -> DefaultListsFragment()
            MoviesListFragment::class.java.name -> MoviesListFragment(viewModelFactory)
            MoviesGalleryFragment::class.java.name -> MoviesGalleryFragment()
            MovieModalBottomSheet::class.java.name -> MovieModalBottomSheet()
            MovieDetailsFragment::class.java.name -> MovieDetailsFragment(viewModelFactory)
            SaveMovieFragment::class.java.name -> SaveMovieFragment(viewModelFactory)
            SearchMovieFragment::class.java.name -> SearchMovieFragment(viewModelFactory)
            UserListsFragment::class.java.name -> UserListsFragment(viewModelFactory)
            StoredListFragment::class.java.name -> StoredListFragment(viewModelFactory)
            else -> super.instantiate(classLoader, className)
        }
    }
}