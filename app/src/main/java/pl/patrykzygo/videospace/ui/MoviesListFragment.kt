package pl.patrykzygo.videospace.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pl.patrykzygo.videospace.databinding.MainFragmentBinding
import retrofit2.HttpException

@AndroidEntryPoint
class MoviesListFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MoviesListViewModel by viewModels()
    val adapter = createRecyclerViewAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        val recyclerView = binding.mostPopularRecycler.moviesListRecycler
        observeMovies()
        collectLoadingState()
        recyclerView.adapter = adapter

        return binding.root
    }

    private fun observeMovies() {
        viewModel.getMovies().observe(viewLifecycleOwner, Observer {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        })
    }

    private fun createRecyclerViewAdapter(): MoviesListRecyclerAdapter {
        val adapter =
            MoviesListRecyclerAdapter(MoviesListRecyclerAdapter.OnMovieClickListener { movie, view ->
                if (movie != null) {
                    Snackbar.make(view, "Movie: ${movie.title} was clicked", Snackbar.LENGTH_LONG)
                        .show()
                }
            })
        return adapter
    }

    private fun collectLoadingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                if (loadStates.refresh is LoadState.Error) {
                    val error = loadStates.refresh as LoadState.Error
                    try {
                        val exception = error.error as HttpException
                        binding.errorMessageTextView.text = exception.message()
                    } catch (e: Exception) {
                        binding.errorMessageTextView.text = error.error.localizedMessage
                    }
                    binding.errorMessageTextView.visibility = View.VISIBLE
                }
            }
        }
    }
}