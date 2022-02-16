package pl.patrykzygo.videospace.ui.movies_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.databinding.FragmentMoviesListBinding
import pl.patrykzygo.videospace.ui.movie_dialogs.MovieModalBottomSheet

@AndroidEntryPoint
class MoviesListFragment : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    val binding get() = _binding!!
    lateinit var viewModel: MoviesListViewModel
    val adapter = createRecyclerViewAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[MoviesListViewModel::class.java]
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