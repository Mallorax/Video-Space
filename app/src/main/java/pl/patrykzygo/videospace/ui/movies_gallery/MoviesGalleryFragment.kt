package pl.patrykzygo.videospace.ui.movies_gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pl.patrykzygo.videospace.databinding.FragmentMoviesGalleryBinding
import pl.patrykzygo.videospace.ui.movie_dialogs.MovieModalBottomSheet

@AndroidEntryPoint
class MoviesGalleryFragment : Fragment() {

    private var _binding: FragmentMoviesGalleryBinding? = null
    val binding get() = _binding!!
    val viewModel: MoviesGalleryViewModel by viewModels()
    val adapter = createRecyclerViewAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesGalleryBinding.inflate(inflater, container, false)
        val requestType = arguments?.getString("request_type")
        val movieId = arguments?.getInt("movieId")
        requestType?.let { viewModel.setRequestType(it, movieId ?: -1) }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        val recyclerView = binding.moviesListRecycler
        recyclerView.adapter = adapter

        observeMovies()

    }

    private fun observeMovies() {
        viewModel.getMovies().observe(viewLifecycleOwner, Observer {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        })
        viewModel.requestType.observe(viewLifecycleOwner, Observer {
            binding.listLabel.text = it
        })
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { states ->
                if (states.refresh is LoadState.Loading) {
                    showContent()
                } else if (states.refresh is LoadState.Error) {
                    showError()
                }
            }
        }
    }

    private fun showContent() {
        binding.listLabel.visibility = View.VISIBLE
        binding.moviesListRecycler.visibility = View.VISIBLE
        binding.noConnectionIcon.visibility = View.INVISIBLE
        binding.noConnectionTextview.visibility = View.INVISIBLE
    }

    private fun showError() {
        binding.listLabel.visibility = View.INVISIBLE
        binding.moviesListRecycler.visibility = View.INVISIBLE
        binding.noConnectionIcon.visibility = View.VISIBLE
        binding.noConnectionTextview.visibility = View.VISIBLE
    }

    private fun createRecyclerViewAdapter(): MoviesGalleryRecyclerAdapter {
        val adapter =
            MoviesGalleryRecyclerAdapter(MoviesGalleryRecyclerAdapter.OnMovieClickListener { movie, _ ->
                val bundle = Bundle()
                bundle.putParcelable("movie", movie)
                val modalBottomSheet = MovieModalBottomSheet()
                modalBottomSheet.arguments = bundle
                modalBottomSheet.show(parentFragmentManager, MovieModalBottomSheet.TAG)
            })
        return adapter
    }

}