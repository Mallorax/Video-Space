package pl.patrykzygo.videospace.ui.movie_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
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
        setListeners()
    }

    private fun displayGenres(genres: List<Genre>) {
        genres.forEach { genre ->
            showIncludeChipInGroup(genre.genreName)
            showExcludeChipInGroup(genre.genreName)
        }
    }

    private fun setListeners() {
        binding.includeGenresChipGroup.forEach { chip ->
            (chip as Chip).setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.addIncludedGenres(chip.text.toString())
                    Snackbar.make(requireView(), chip.text.toString(), Snackbar.LENGTH_LONG).show()
                } else {
                    viewModel.removeIncludedGenres(chip.text.toString())
                }
            }
        }

        binding.excludeGenresChipGroup.forEach { chip ->
            (chip as Chip).setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.addExcludedGenres(chip.text.toString())
                    Snackbar.make(requireView(), chip.text.toString(), Snackbar.LENGTH_LONG).show()
                } else {
                    viewModel.removeExcludedGenres(chip.text.toString())
                }
            }
        }
    }

    private fun showIncludeChipInGroup(chipText: String){
        val chip = layoutInflater.inflate(R.layout.layout_include_chip_choice, binding.includeGenresChipGroup, false) as Chip
        chip.text = chipText
        binding.includeGenresChipGroup.addView(chip)
    }

    private fun showExcludeChipInGroup(chipText: String){
        val chip = layoutInflater.inflate(R.layout.layout_exclude_chip_choice, binding.excludeGenresChipGroup, false) as Chip
        chip.text = chipText
        binding.excludeGenresChipGroup.addView(chip)
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