package pl.patrykzygo.videospace.ui.save_movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.databinding.FragmentSaveMovieBinding
import pl.patrykzygo.videospace.ui.MainViewModelFactory
import pl.patrykzygo.videospace.ui.delegate.AppBarDelegate
import pl.patrykzygo.videospace.ui.delegate.AppBarDelegateImpl

@AndroidEntryPoint
class SaveMovieFragment constructor(private val viewModelFactory: MainViewModelFactory) :
    Fragment(),
    AppBarDelegate by AppBarDelegateImpl() {

    private var _binding: FragmentSaveMovieBinding? = null
    val binding get() = _binding!!
    lateinit var viewModel: SaveMovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveMovieBinding.inflate(inflater, container, false)
        viewModel = viewModelFactory.create(SaveMovieViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAppBar(findNavController(), binding.appBar.toolbar)
        binding.saveMovieTitleTextview.text =
            arguments?.let { SaveMovieFragmentArgs.fromBundle(it).movieTitle }
        setListeners()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewModel.inputErrorMessage.observe(viewLifecycleOwner, Observer {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        })
    }

    private fun setListeners() {
        binding.saveMovieFab.setOnClickListener {
            val id = arguments?.let { SaveMovieFragmentArgs.fromBundle(it).id }
            val title = arguments?.let { SaveMovieFragmentArgs.fromBundle(it).movieTitle }
            val status = getCheckedChipText()
            val score = binding.saveMovieScorePicker.value
            viewModel.saveMovie(id, title, status, score)
        }
    }

    private fun getCheckedChipText(): String?{
        val checkedChipId = binding.saveMovieChipGroup.checkedChipId
        val checkedChip = requireView().findViewById<Chip>(checkedChipId)
        return checkedChip?.text?.toString()
    }
}