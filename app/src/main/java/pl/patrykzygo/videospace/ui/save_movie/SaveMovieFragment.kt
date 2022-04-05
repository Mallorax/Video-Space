package pl.patrykzygo.videospace.ui.save_movie

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
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.constants.MovieStatus
import pl.patrykzygo.videospace.databinding.FragmentSaveMovieBinding
import pl.patrykzygo.videospace.delegate.ui.AppBarDelegate
import pl.patrykzygo.videospace.delegate.ui.AppBarDelegateImpl

@AndroidEntryPoint
class SaveMovieFragment :
    Fragment(),
    AppBarDelegate by AppBarDelegateImpl() {

    private var _binding: FragmentSaveMovieBinding? = null
    val binding get() = _binding!!
    val viewModel: SaveMovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveMovieBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.getMovieToSave(arguments?.let { SaveMovieFragmentArgs.fromBundle(it).id } ?: -1)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAppBar(findNavController(), binding.appBar.toolbar)
        binding.bottomNavViewLayout.bottomNavView.setupWithNavController(findNavController())
        setListeners()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewModel.inputFeedbackMessage.observe(viewLifecycleOwner, Observer {
            showSnackbarWithText(it)
        })
        viewModel.errorFeedbackMessage.observe(viewLifecycleOwner, Observer {
            showSnackbarWithText(it)
        })
        viewModel.selectedStatus.observe(viewLifecycleOwner) {
            if (it == MovieStatus.COMPLETED || it == MovieStatus.DROPPED) {
                binding.saveMovieScorePicker.visibility = View.VISIBLE
                binding.saveMovieScoreTextview.visibility = View.VISIBLE

            } else {
                binding.saveMovieScorePicker.visibility = View.GONE
                binding.saveMovieScoreTextview.visibility = View.GONE
            }
        }
        viewModel.movie.observe(viewLifecycleOwner){
            binding.saveMovieTitleTextview.text = it.title
            binding.releaseDateTextview.text = "Release date: ${it.releaseDate}"
        }
    }

    private fun showSnackbarWithText(text: String){
        Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG)
            .setAnchorView(binding.bottomAppBar)
            .show()
    }

    private fun setListeners() {
        binding.saveMovieFab.setOnClickListener {
            val userText = binding.saveMovieNotesTextInput.text.toString()
            val score = binding.saveMovieScorePicker.value
            viewModel.saveMovie(score, userText)
        }
        binding.saveMovieChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val chip = group.findViewById<Chip>(checkedIds.first())
                viewModel.selectStatus(chip.text.toString())
            }
        }
    }
}