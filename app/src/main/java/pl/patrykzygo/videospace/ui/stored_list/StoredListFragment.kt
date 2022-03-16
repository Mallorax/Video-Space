package pl.patrykzygo.videospace.ui.stored_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.databinding.FragmentStoredListBinding
import pl.patrykzygo.videospace.ui.factories.MainViewModelFactory

@AndroidEntryPoint
class StoredListFragment(
    private val viewModelFactory: MainViewModelFactory,
    private val status: String
) : Fragment() {

    private var _binding: FragmentStoredListBinding? = null
    val binding get() = _binding!!
    private val adapter = createRecyclerViewAdapter()

    lateinit var viewModel: StoredListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoredListBinding.inflate(inflater, container, false)
        viewModel = viewModelFactory.create(StoredListViewModel::class.java)
        viewModel.getMoviesWithStatus(status)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.storedListRecycler
        recyclerView.adapter = adapter

        subscribeObservers()
    }
    private fun createRecyclerViewAdapter(): StoredListAdapter{
        return StoredListAdapter(StoredListAdapter.OnMovieClickListener{ movie, view ->
            val bundle = Bundle()
            bundle.putParcelable("movie", movie)
            parentFragmentManager.setFragmentResult("storedListResult", bundle)
        })
    }

    private fun subscribeObservers(){
        viewModel.movies.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        })
    }
}