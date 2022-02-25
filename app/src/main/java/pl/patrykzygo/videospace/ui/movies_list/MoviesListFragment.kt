package pl.patrykzygo.videospace.ui.movies_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.databinding.FragmentMoviesListBinding
import pl.patrykzygo.videospace.ui.MainViewModelFactory
import pl.patrykzygo.videospace.ui.movie_dialogs.MovieModalBottomSheet

@AndroidEntryPoint
class MoviesListFragment(val viewModelFactory: MainViewModelFactory) : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    val binding get() = _binding!!
    lateinit var viewModel: MoviesListViewModel
    val adapter = createRecyclerViewAdapter()

    lateinit var movieGenre: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        viewModel = viewModelFactory.create(MoviesListViewModel::class.java)
        movieGenre = MoviesListFragmentArgs.fromBundle(requireArguments()).genre

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        val recyclerView = binding.moviesListVerticalRecycler
        recyclerView.adapter = adapter

        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.getMoviesInGenre(movieGenre).observe(viewLifecycleOwner, Observer { moviesList ->
            adapter.submitData(viewLifecycleOwner.lifecycle, moviesList)
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