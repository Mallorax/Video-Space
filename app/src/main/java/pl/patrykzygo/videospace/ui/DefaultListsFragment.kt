package pl.patrykzygo.videospace.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.databinding.FragmentDefaultListsBinding
import pl.patrykzygo.videospace.repository.MoviesRequestType
import pl.patrykzygo.videospace.ui.movies_list.MoviesListFragment

@AndroidEntryPoint
class DefaultListsFragment : Fragment() {

    private var _binding: FragmentDefaultListsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDefaultListsBinding.inflate(inflater)
        setUpAppBar()
        parentFragmentManager.setFragmentResultListener("movieResult", this) { requestKey, bundle ->
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
        addMoviesListFragmentToContainer(
            binding.mostPopularMoviesContainer.id,
            MoviesRequestType.POPULAR,
            "Most popular"
        )
        addMoviesListFragmentToContainer(
            binding.topRatedMoviesContainer.id,
            MoviesRequestType.TOP_RATED,
            "Top rated"
        )

        addMoviesListFragmentToContainer(
            binding.nowPlayingMoviesContainer.id,
            MoviesRequestType.NOW_PLAYING,
            "Now playing"
        )

        addMoviesListFragmentToContainer(
            binding.upcomingMoviesContainer.id,
            MoviesRequestType.UPCOMING,
            "Upcoming"
        )
    }

    private fun addMoviesListFragmentToContainer(
        containerId: Int,
        contentType: MoviesRequestType,
        listLabel: String
    ) {
        val fragmentManager = parentFragmentManager
        fragmentManager.commit {
            val args = Bundle()
            args.putSerializable("request_type", contentType)
            args.putString("list_label", listLabel)
            val fragment = MoviesListFragment()
            fragment.arguments = args
            replace(containerId, fragment)
            setReorderingAllowed(true)
        }
    }

    private fun setUpAppBar() {
        val navController = findNavController()
        val appBarConfig = AppBarConfiguration(navController.graph)
        binding.appBar.toolbar.setupWithNavController(navController, appBarConfig)
    }
}