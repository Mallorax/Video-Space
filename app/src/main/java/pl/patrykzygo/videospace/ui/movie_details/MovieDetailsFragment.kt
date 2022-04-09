package pl.patrykzygo.videospace.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.constants.MoviesRequestType
import pl.patrykzygo.videospace.databinding.FragmentMovieDetailsBinding
import pl.patrykzygo.videospace.delegate.ui.AppBarDelegate
import pl.patrykzygo.videospace.delegate.ui.AppBarDelegateImpl
import pl.patrykzygo.videospace.ui.movies_gallery.MoviesGalleryFragment

@AndroidEntryPoint
class MovieDetailsFragment :
    Fragment(),
    AppBarDelegate by AppBarDelegateImpl() {

    private var _binding: FragmentMovieDetailsBinding? = null
    val binding get() = _binding!!
    val viewModel: MovieDetailsViewModel by viewModels()
    var movieId: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        movieId = arguments?.let { MovieDetailsFragmentArgs.fromBundle(it).movieId }
        setFragmentResultListener()
        setHasOptionsMenu(true)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFragmentContainers()
        setOnClickListeners()
        subscribeObservers()
        viewModel.setMovie(movieId)
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

    fun addMoviesListFragmentToContainer(
        containerId: Int,
        contentType: String,
        fragment: Fragment
    ) {
        val fragmentManager = parentFragmentManager
        fragmentManager.commit {
            val args = Bundle()
            args.putString("request_type", contentType)
            args.putInt("movieId", movieId ?: -1)
            fragment.arguments = args
            replace(containerId, fragment)
            setReorderingAllowed(true)
        }
    }


    private fun setFragmentResultListener() {
        parentFragmentManager.setFragmentResultListener("movieResult", this) { _, bundle ->
            val movieId = bundle.getInt("movie")
            val action = MovieDetailsFragmentDirections.actionMovieDetailsSelf(movieId)
            findNavController().navigate(action)

        }
    }

    private fun setOnClickListeners() {
        binding.detailsFab.setOnClickListener {
            viewModel.launchSaveMovieEvent()
        }
    }

    private fun subscribeObservers() {
        viewModel.genres.observe(viewLifecycleOwner) {
            showMovieGenres(it)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        }
        viewModel.saveMovieEvent.observe(viewLifecycleOwner) {
            val action = MovieDetailsFragmentDirections.actionMovieDetailsToSaveMovieFragment(
                it.id
            )
            findNavController().navigate(action)
        }
        viewModel.searchInGenreLiveEvent.observe(viewLifecycleOwner) {
            val action = MovieDetailsFragmentDirections.actionMovieDetailsToMoviesListFragment(it)
            findNavController().navigate(action)
        }
        viewModel.searchInGenreErrorMessage.observe(viewLifecycleOwner) {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
//                binding.appBar.appBarProgressBar.visibility = View.VISIBLE
            } else {
//                binding.appBar.appBarProgressBar.visibility = View.GONE
            }
        }
    }

    private fun showMovieGenres(genres: List<String>?) {
        genres?.forEach { genre ->
            val chip = Chip(requireContext())
            chip.text = genre
            chip.setChipBackgroundColorResource(R.color.purple_200)
            chip.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
            binding.genresChipGroup.addView(chip)
            chip.setOnClickListener {
                viewModel.moveToGenreList(genre)
            }
        }
    }

}