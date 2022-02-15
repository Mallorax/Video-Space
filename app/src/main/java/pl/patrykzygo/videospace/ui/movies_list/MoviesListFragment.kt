package pl.patrykzygo.videospace.ui.movies_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.databinding.FragmentMoviesListBinding
import pl.patrykzygo.videospace.repository.MoviesRequestType
import pl.patrykzygo.videospace.ui.movie_dialogs.MovieModalBottomSheet

@AndroidEntryPoint
class MoviesListFragment : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    val binding get() = _binding!!
    private val viewModel: MoviesListViewModel by viewModels()
    val adapter = createRecyclerViewAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        val requestType = arguments?.get("request_type") as MoviesRequestType?
        val movieId = arguments?.get("movieId") as Int?
        requestType?.let { viewModel.setRequestType(it, movieId ?: -1) }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        val listLabel = arguments?.getString("list_label")
        listLabel?.let { binding.listLabel.text = listLabel }

        val recyclerView = binding.moviesListRecycler
        recyclerView.adapter = adapter

        observeMovies()

    }

    private fun observeMovies() {
        viewModel.getMovies().observe(viewLifecycleOwner, Observer {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        })
    }

    private fun createRecyclerViewAdapter(): MoviesListRecyclerAdapter {
        val adapter =
            MoviesListRecyclerAdapter(MoviesListRecyclerAdapter.OnMovieClickListener { movie, view ->
                val bundle = Bundle()
                bundle.putParcelable("movie", movie)
                val modalBottomSheet = MovieModalBottomSheet()
                modalBottomSheet.arguments = bundle
                modalBottomSheet.show(parentFragmentManager, MovieModalBottomSheet.TAG)
            })
        return adapter
    }

}