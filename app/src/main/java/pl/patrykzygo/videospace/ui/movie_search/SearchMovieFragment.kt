package pl.patrykzygo.videospace.ui.movie_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.data.app.Genre
import pl.patrykzygo.videospace.databinding.FragmentMovieSearchBinding
import pl.patrykzygo.videospace.ui.MainViewModelFactory
import pl.patrykzygo.videospace.ui.delegate.AppBarDelegate
import pl.patrykzygo.videospace.ui.delegate.AppBarDelegateImpl

@AndroidEntryPoint
class SearchMovieFragment(val viewModelFactory: MainViewModelFactory) : Fragment(),
    AppBarDelegate by AppBarDelegateImpl() {

    private var _binding: FragmentMovieSearchBinding? = null
    val binding get() = _binding!!

    lateinit var viewModel: SearchMovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieSearchBinding.inflate(inflater, container, false)
        viewModel = viewModelFactory.create(SearchMovieViewModel::class.java)
        viewModel.getAllGenres()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAppBar(findNavController(), binding.appBar.toolbar)
        subscribeObservers()
    }

    private fun displayGenres(genres: List<Genre>) {
        genres.forEach { genre ->
            binding.includeGenresChipGroup.addView(createGenreChip(genre.genreName))
            binding.excludeGenresChipGroup.addView(createGenreChip(genre.genreName))
        }
    }

    private fun createGenreChip(text: String): Chip {
        val chip = Chip(requireContext())
        chip.text = text
        chip.setChipBackgroundColorResource(R.color.purple_200)
        chip.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
        return chip
    }

    private fun subscribeObservers() {
        viewModel.genres.observe(viewLifecycleOwner, Observer {
            displayGenres(it)
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        })
    }
}