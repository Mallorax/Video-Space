package pl.patrykzygo.videospace.ui.movies_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.databinding.FragmentMoviesListBinding
import pl.patrykzygo.videospace.ui.MainViewModelFactory
import pl.patrykzygo.videospace.ui.movie_dialogs.MovieModalBottomSheet
import pl.patrykzygo.videospace.ui.movies_gallery.MoviesGalleryRecyclerAdapter
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MoviesListFragment: Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    val binding get() = _binding!!
    lateinit var viewModel: MoviesListViewModel
    val adapter = createRecyclerViewAdapter()

    @Inject
    @Named("main_vm_factory")
    lateinit var viewModelFactory: MainViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        viewModel = viewModelFactory.create(MoviesListViewModel::class.java)
        val genre = arguments?.getString("genre")
        genre?.let { viewModel.setGenre(it) }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        val recyclerView = binding.moviesListVerticalRecycler
        recyclerView.adapter = adapter

        subscribeObservers()
    }

    private fun subscribeObservers(){

    }

    private fun createRecyclerViewAdapter(): MoviesGalleryRecyclerAdapter {
        val adapter =
            MoviesGalleryRecyclerAdapter(MoviesGalleryRecyclerAdapter.OnMovieClickListener { movie, view ->
                val bundle = Bundle()
                bundle.putParcelable("movie", movie)
                val modalBottomSheet = MovieModalBottomSheet()
                modalBottomSheet.arguments = bundle
                modalBottomSheet.show(parentFragmentManager, MovieModalBottomSheet.TAG)
            })
        return adapter
    }
}