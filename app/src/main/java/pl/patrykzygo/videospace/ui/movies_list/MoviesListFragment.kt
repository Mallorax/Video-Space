package pl.patrykzygo.videospace.ui.movies_list

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.databinding.FragmentMoviesListBinding
import pl.patrykzygo.videospace.ui.MainViewModelFactory
import pl.patrykzygo.videospace.ui.delegate.AppBarDelegate
import pl.patrykzygo.videospace.ui.delegate.AppBarDelegateImpl

@AndroidEntryPoint
class MoviesListFragment(val viewModelFactory: MainViewModelFactory) : Fragment(),
    AppBarDelegate by AppBarDelegateImpl() {

    private var _binding: FragmentMoviesListBinding? = null
    val binding get() = _binding!!
    lateinit var viewModel: MoviesListViewModel
    private val adapter = createRecyclerViewAdapter()

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
        setUpAppBar(findNavController(), binding.toolbar)
        binding.lifecycleOwner = viewLifecycleOwner
        val recyclerView = binding.moviesListVerticalRecycler
        recyclerView.adapter = adapter

        subscribeObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private fun subscribeObservers() {
        viewModel.getMoviesInGenre(movieGenre).observe(viewLifecycleOwner, Observer { moviesList ->
            adapter.submitData(viewLifecycleOwner.lifecycle, moviesList)
        })
    }


    private fun createRecyclerViewAdapter(): MoviesListRecyclerAdapter {
        val adapter =
            MoviesListRecyclerAdapter(MoviesListRecyclerAdapter.OnMovieClickListener { movie, _ ->
                if (movie != null) {
                    val action =
                        MoviesListFragmentDirections.actionMoviesListFragmentToMovieDetails(movie)
                    findNavController().navigate(action)
                }

            })
        return adapter
    }
}