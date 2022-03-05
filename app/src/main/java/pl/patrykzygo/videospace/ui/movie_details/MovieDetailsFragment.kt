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
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.databinding.FragmentMovieDetailsBinding
import pl.patrykzygo.videospace.others.MoviesRequestType
import pl.patrykzygo.videospace.ui.MainViewModelFactory
import pl.patrykzygo.videospace.ui.delegate.AppBarDelegate
import pl.patrykzygo.videospace.ui.delegate.AppBarDelegateImpl
import pl.patrykzygo.videospace.ui.movies_gallery.MoviesGalleryFragment

@AndroidEntryPoint
class MovieDetailsFragment constructor(private val viewModelFactory: MainViewModelFactory) :
    Fragment(),
    AppBarDelegate by AppBarDelegateImpl() {

    private var _binding: FragmentMovieDetailsBinding? = null
    val binding get() = _binding!!
    lateinit var viewModel: MovieDetailsViewModel
    var movie: Movie? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        movie = arguments?.let { MovieDetailsFragmentArgs.fromBundle(it).movie }
        viewModel = viewModelFactory.create(MovieDetailsViewModel::class.java)
        setFragmentResultListener()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAppBar(findNavController(), binding.appBar.toolbar)
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
            MoviesGalleryFragment()
        )
        binding.similarMoviesContainer.visibility = View.VISIBLE
        addMoviesListFragmentToContainer(
            binding.similarMoviesContainer.id,
            MoviesRequestType.SIMILAR,
            MoviesGalleryFragment()
        )
    }

    private fun setFragmentResultListener(){
        parentFragmentManager.setFragmentResultListener("movieResult", this) { _, bundle ->
            val movie = bundle.getParcelable<Movie>("movie")
            if (movie != null) {
                val action = MovieDetailsFragmentDirections.actionMovieDetailsSelf(movie)
                findNavController().navigate(action)
            }
        }
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

    private fun setOnClickListeners() {
        binding.addToFavouritesButton.setOnClickListener {
            viewModel.toggleFavourite()
        }

        binding.detailsFab.setOnClickListener {
            viewModel.saveMovieEvent()
        }
    }

    private fun observeViewModel() {
        viewModel.movie.observe(viewLifecycleOwner, Observer {
            updateFavouritesButton(it?.isFavourite)
        })
        viewModel.genres.observe(viewLifecycleOwner, Observer {
            showMovieGenres(it)
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        })
        viewModel.saveMovieEvent.observe(viewLifecycleOwner, Observer {
            val action = MovieDetailsFragmentDirections.actionMovieDetailsToSaveMovieFragment(it.id, it.title)
            findNavController().navigate(action)
        })
    }

    private fun showMovieGenres(genres: List<String>?) {
        genres?.forEach { genre ->
            val chip = Chip(requireContext())
            chip.text = genre
            chip.setChipBackgroundColorResource(R.color.purple_200)
            chip.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
            binding.genresChipGroup.addView(chip)
            chip.setOnClickListener {
                val action =
                    MovieDetailsFragmentDirections.actionMovieDetailsToMoviesListFragment(genre)
                findNavController().navigate(action)
            }
        }
    }

    private fun updateFavouritesButton(isFavourite: Boolean?) {
        if (isFavourite == true) {
            binding.addToFavouritesButton.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_baseline_favorite,
                0,
                0,
                0
            )
            binding.addToFavouritesButton.setBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.purple_200,
                    null
                )
            )
        } else {
            binding.addToFavouritesButton.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_baseline_favorite_border,
                0,
                0,
                0
            )
            binding.addToFavouritesButton.setBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.white,
                    null
                )
            )
        }
    }
}