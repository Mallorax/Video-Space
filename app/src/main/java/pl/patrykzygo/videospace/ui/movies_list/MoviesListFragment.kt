package pl.patrykzygo.videospace.ui.movies_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
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
        setUpAppBar()
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

    private fun setUpAppBar() {
        val navController = findNavController()
        val appBarConfig = AppBarConfiguration(navController.graph)
        binding.appBar.toolbar.setupWithNavController(navController, appBarConfig)
    }

    private fun createRecyclerViewAdapter(): MoviesListRecyclerAdapter {
        val adapter =
            MoviesListRecyclerAdapter(MoviesListRecyclerAdapter.OnMovieClickListener { movie, view ->
                if (movie != null){
                    val action = MoviesListFragmentDirections.actionMoviesListFragmentToMovieDetails(movie)
                    findNavController().navigate(action)
                }

            })
        return adapter
    }
}