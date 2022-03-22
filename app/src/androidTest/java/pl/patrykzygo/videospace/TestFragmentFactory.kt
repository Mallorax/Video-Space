package pl.patrykzygo.videospace

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.navigation.testing.TestNavHostController
import pl.patrykzygo.videospace.ui.DefaultListsFragment
import pl.patrykzygo.videospace.ui.movie_details.MovieDetailsFragment
import pl.patrykzygo.videospace.ui.movies_gallery.MoviesGalleryFragment
import pl.patrykzygo.videospace.util.provideMovieWithIdUi
import javax.inject.Inject

class TestFragmentFactory @Inject constructor(
    val navController: TestNavHostController
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            DefaultListsFragment::class.java.name -> DefaultListsFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever {
                    if (it != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
            MovieDetailsFragment::class.java.name -> MovieDetailsFragment().also { fragment ->
                navController.setCurrentDestination(R.id.movie_details, bundleOf("movieId" to 1))
                fragment.viewLifecycleOwnerLiveData.observeForever {
                    if (it != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
            MoviesGalleryFragment::class.java.name -> MoviesGalleryFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}