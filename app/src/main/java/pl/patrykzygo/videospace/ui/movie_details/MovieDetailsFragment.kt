package pl.patrykzygo.videospace.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.databinding.FragmentMovieDetailsBinding
import pl.patrykzygo.videospace.others.MoviesRequestType
import pl.patrykzygo.videospace.ui.movies_list.MoviesListFragment

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    val binding get() = _binding!!
    lateinit var viewModel: MovieDetailsViewModel
    var movie: Movie? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        movie = arguments?.let { MovieDetailsFragmentArgs.fromBundle(it).movie }
        viewModel = MovieDetailsViewModel()
        parentFragmentManager.setFragmentResultListener("movieResult", this) { _, bundle ->
            val movie = bundle.getParcelable<Movie>("movie")
            if (movie != null) {
                val action = MovieDetailsFragmentDirections.actionMovieDetailsSelf(movie)
                findNavController().navigate(action)
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAppBar()
        setUpFragmentContainers()
        setOnClickListeners()
        observeViewModel()
        viewModel.setMovie(movie)
    }

    private fun setUpFragmentContainers() {
        binding.recommendedMoviesContainer.visibility = View.VISIBLE
        addMoviesListFragmentToContainer(
            binding.recommendedMoviesContainer.id,
            MoviesRequestType.RECOMMENDATIONS,
            MoviesListFragment()
        )
        binding.similarMoviesContainer.visibility = View.VISIBLE
        addMoviesListFragmentToContainer(
            binding.similarMoviesContainer.id,
            MoviesRequestType.SIMILAR,
            MoviesListFragment()
        )
    }

    private fun setUpAppBar() {
        val navController = findNavController()
        val appBarConfig = AppBarConfiguration(navController.graph)
        binding.appBar.toolbar.setupWithNavController(navController, appBarConfig)
    }

    fun addMoviesListFragmentToContainer(
        containerId: Int,
        contentType: String,
        fragment: Fragment
    ) {
        val fragmentManager = parentFragmentManager
        fragmentManager.commit {
            val args = Bundle()
            args.putString("request_type", contentType)
            args.putInt("movieId", movie?.id ?: -1)
            fragment.arguments = args
            replace(containerId, fragment)
            setReorderingAllowed(true)
        }
    }

    private fun setOnClickListeners(){
        binding.addToFavouritesButton.setOnClickListener {
            viewModel.toggleFavourite()
        }
    }

    private fun observeViewModel(){
        viewModel.movie.observe(viewLifecycleOwner, Observer {
            updateFavouritesButton(it?.isFavourite)
        })
    }

    private fun updateFavouritesButton(isFavourite: Boolean?){
        if (isFavourite == true){
            binding.addToFavouritesButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_favorite, 0 , 0, 0)
            binding.addToFavouritesButton.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.purple_200, null))
        }else{
            binding.addToFavouritesButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_favorite_border, 0 , 0, 0)
            binding.addToFavouritesButton.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.white, null))
        }
    }
}