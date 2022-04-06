package pl.patrykzygo.videospace.ui.movie_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.chip.Chip
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.data.app.Genre
import pl.patrykzygo.videospace.databinding.FragmentMovieSearchBinding
import pl.patrykzygo.videospace.delegate.ui.AppBarDelegate
import pl.patrykzygo.videospace.delegate.ui.AppBarDelegateImpl

@AndroidEntryPoint
class SearchMovieFragment : Fragment(),
    AppBarDelegate by AppBarDelegateImpl() {

    private var _binding: FragmentMovieSearchBinding? = null
    val binding get() = _binding!!

    val viewModel: SearchMovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieSearchBinding.inflate(inflater, container, false)
        viewModel.getAllGenre()
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAppBar(findNavController(), binding.appBar.toolbar)
        binding.bottomNavViewLayout.bottomNavView.setupWithNavController(findNavController())
        subscribeObservers()
        binding.searchFragmentFab.setOnClickListener {
            viewModel.submitRequest(
                binding.searchScorePicker.value,
                binding.voteCountTextInput.text.toString()
            )
        }
    }

    private fun displayGenres(genres: List<Genre>) {
        genres.forEach { genre ->
            showIncludeChipInGroup(genre.genreName)
            showExcludeChipInGroup(genre.genreName)
        }
    }


    private fun onIncludeChipClickAction(isChecked: Boolean, chipText: String) {
        if (isChecked) {
            viewModel.addIncludedGenre(chipText)
        } else {
            viewModel.removeIncludedGenre(chipText)
        }
    }

    private fun onExcludeChipClickAction(isChecked: Boolean, chipText: String) {
        if (isChecked) {
            viewModel.addExcludedGenre(chipText)
        } else {
            viewModel.removeExcludedGenre(chipText)
        }
    }

    private fun showIncludeChipInGroup(chipText: String) {
        val chip = layoutInflater.inflate(
            R.layout.layout_include_chip_choice,
            binding.includeGenresChipGroup,
            false
        ) as Chip
        chip.text = chipText
        chip.setOnCheckedChangeListener { _, isChecked ->
            onIncludeChipClickAction(isChecked, chip.text.toString())
        }
        binding.includeGenresChipGroup.addView(chip)
    }

    private fun showExcludeChipInGroup(chipText: String) {
        val chip = layoutInflater.inflate(
            R.layout.layout_exclude_chip_choice,
            binding.excludeGenresChipGroup,
            false
        ) as Chip
        chip.text = chipText
        chip.setOnCheckedChangeListener { _, isChecked ->
            onExcludeChipClickAction(isChecked, chip.text.toString())
        }
        binding.excludeGenresChipGroup.addView(chip)
    }


    private fun subscribeObservers() {
        viewModel.genres.observe(viewLifecycleOwner, Observer {
            displayGenres(it)
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            showSnackbarWithTextAndBottomAppBar(it, binding.bottomAppBar)
        })
        viewModel.submitRequestInputErrorMessage.observe(viewLifecycleOwner, Observer {
            showSnackbarWithTextAndBottomAppBar(it, binding.bottomAppBar)
        })
        viewModel.requestMoviesLiveEvent.observe(viewLifecycleOwner, Observer {
            val action =
                SearchMovieFragmentDirections.actionSearchMovieFragmentToMoviesListFragment(it)
            findNavController().navigate(action)
        })
    }

}