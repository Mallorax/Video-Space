package pl.patrykzygo.videospace.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.constants.MoviesRequestType
import pl.patrykzygo.videospace.databinding.FragmentHomeBinding
import pl.patrykzygo.videospace.delegate.ui.AppBarDelegate
import pl.patrykzygo.videospace.delegate.ui.AppBarDelegateImpl
import pl.patrykzygo.videospace.ui.movies_gallery.MoviesGalleryFragment

@AndroidEntryPoint
class HomeFragment() : Fragment(),
    AppBarDelegate by AppBarDelegateImpl() {

    private var _binding: FragmentHomeBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)
        parentFragmentManager.setFragmentResultListener(
            "movieResult",
            viewLifecycleOwner
        ) { _, bundle ->
            val movieId = bundle.getInt("movie")
            val action =
                HomeFragmentDirections.actionMainFragmentToMovieDetails(movieId)
            findNavController().navigate(action)
            setHasOptionsMenu(true)

            enterTransition = MaterialFadeThrough()
            exitTransition = MaterialFadeThrough()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addFragmentToContainer(
            binding.mostPopularMoviesContainer.id,
            MoviesRequestType.POPULAR,
            MoviesGalleryFragment()
        )
        addFragmentToContainer(
            binding.topRatedMoviesContainer.id,
            MoviesRequestType.TOP_RATED,
            MoviesGalleryFragment()
        )

        addFragmentToContainer(
            binding.nowPlayingMoviesContainer.id,
            MoviesRequestType.NOW_PLAYING,
            MoviesGalleryFragment()
        )

        addFragmentToContainer(
            binding.upcomingMoviesContainer.id,
            MoviesRequestType.UPCOMING,
            MoviesGalleryFragment()
        )
    }

    fun addFragmentToContainer(
        containerId: Int,
        contentType: String,
        fragment: Fragment
    ) {
        val fragmentManager = parentFragmentManager
        fragmentManager.commit {
            val args = Bundle()
            args.putString("request_type", contentType)
            fragment.arguments = args
            replace(containerId, fragment)
            setReorderingAllowed(true)
        }
    }

}