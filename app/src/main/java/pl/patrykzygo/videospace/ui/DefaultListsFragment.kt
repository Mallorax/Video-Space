package pl.patrykzygo.videospace.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.databinding.FragmentDefaultListsBinding
import pl.patrykzygo.videospace.others.MoviesRequestType
import pl.patrykzygo.videospace.ui.delegate.AppBarDelegate
import pl.patrykzygo.videospace.ui.delegate.AppBarDelegateImpl
import pl.patrykzygo.videospace.ui.movies_gallery.MoviesGalleryFragment

@AndroidEntryPoint
class DefaultListsFragment() : Fragment(),
    AppBarDelegate by AppBarDelegateImpl() {

    private var _binding: FragmentDefaultListsBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDefaultListsBinding.inflate(inflater)
        parentFragmentManager.setFragmentResultListener("movieResult", this) { _, bundle ->
            val movie = bundle.getParcelable<Movie>("movie")
            if (movie != null) {
                val action = DefaultListsFragmentDirections.actionMainFragmentToMovieDetails(movie)
                findNavController().navigate(action)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAppBar(findNavController(), binding.appBar.toolbar)
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