package pl.patrykzygo.videospace.ui.stored_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.databinding.FragmentStoredListBinding

@AndroidEntryPoint
class StoredListFragment(
    private val status: String
) : Fragment() {

    //TODO status shouldn't be in constructor
    private var _binding: FragmentStoredListBinding? = null
    val binding get() = _binding!!
    private val adapter = createRecyclerViewAdapter()

    val viewModel: StoredListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoredListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.getMoviesWithStatus(status)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.storedListRecycler
        recyclerView.adapter = adapter

        subscribeObservers()
    }

    private fun createRecyclerViewAdapter(): StoredListAdapter {
        return StoredListAdapter(StoredListAdapter.OnMovieClickListener { movie, _ ->
            movie?.let {
                val bundle = Bundle()
                bundle.putInt("movieId", movie.movieId)
                parentFragmentManager.setFragmentResult("storedListResult", bundle)
            }
        })
    }

    private fun subscribeObservers() {
        viewModel.movies.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                binding.root.visibility = View.GONE
            } else {
                binding.root.visibility = View.VISIBLE
            }
            adapter.submitList(it)
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        })
    }
}