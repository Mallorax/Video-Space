package pl.patrykzygo.videospace.ui.movies_gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.databinding.FragmentMoviesGalleryBinding
import pl.patrykzygo.videospace.ui.factories.MainViewModelFactory
import pl.patrykzygo.videospace.ui.movie_dialogs.MovieModalBottomSheet
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MoviesGalleryFragment : Fragment() {

    private var _binding: FragmentMoviesGalleryBinding? = null
    val binding get() = _binding!!
    lateinit var viewModel: MoviesGalleryViewModel
    val adapter = createRecyclerViewAdapter()

    @Inject
    @Named("main_vm_factory")
    lateinit var viewModelFactory: MainViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesGalleryBinding.inflate(inflater, container, false)
        viewModel = viewModelFactory.create(MoviesGalleryViewModel::class.java)
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