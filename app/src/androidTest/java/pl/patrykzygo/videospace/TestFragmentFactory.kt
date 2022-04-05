package pl.patrykzygo.videospace

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import pl.patrykzygo.videospace.ui.HomeFragment
import pl.patrykzygo.videospace.ui.movie_details.MovieDetailsFragment
import pl.patrykzygo.videospace.ui.movie_search.SearchMovieFragment
import pl.patrykzygo.videospace.ui.movies_gallery.MoviesGalleryFragment
import javax.inject.Inject

class TestFragmentFactory @Inject constructor(
    val navController: TestNavHostController
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            HomeFragment::class.java.name -> HomeFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever {
                    it?.let {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
            MovieDetailsFragment::class.java.name -> MovieDetailsFragment().also { fragment ->
                navController.setCurrentDestination(R.id.movie_details, bundleOf("movieId" to 1))
                fragment.viewLifecycleOwnerLiveData.observeForever {
                    it?.let {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
            MoviesGalleryFragment::class.java.name -> MoviesGalleryFragment()
            SearchMovieFragment::class.java.name -> SearchMovieFragment().also { fragment ->
                navController.setCurrentDestination(
                    R.id.search_fragment,
                    bundleOf("id" to 1, "movieTitle" to "test title")
                )
                fragment.viewLifecycleOwnerLiveData.observeForever {
                    it?.let {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}