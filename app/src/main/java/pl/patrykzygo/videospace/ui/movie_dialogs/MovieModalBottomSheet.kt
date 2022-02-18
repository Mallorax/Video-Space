package pl.patrykzygo.videospace.ui.movie_dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.databinding.MovieBottomSheetBinding

@AndroidEntryPoint
open class MovieModalBottomSheet : BottomSheetDialogFragment() {

    private var _binding: MovieBottomSheetBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: MovieBottomSheetViewModel

    companion object {
        const val TAG = "MovieModalBottomSheet"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MovieBottomSheetBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[MovieBottomSheetViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        val movie = arguments?.getParcelable<Movie>("movie")
        viewModel.setMovie(movie)
        observeViewModelState()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie = arguments?.getParcelable<Movie>("movie")
        setListeners(movie)
    }

    private fun observeViewModelState() {
        viewModel.movie.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                updateIsFavouriteImage(it.isFavourite)
                updateIsOnWatchLaterListImage(it.isOnWatchLater)
                binding.bottomSheetTitle.text = it.title
            } else {
                dismiss()
            }

        })
    }


    private fun setListeners(movie: Movie?) {
        binding.likeTextView.setOnClickListener {
            viewModel.changeIsMovieLiked()
        }

        binding.moreInfoTextView.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("movie", movie)
            parentFragmentManager.setFragmentResult("movieResult", bundle)
            dismiss()
        }

        binding.watchLaterListTextView.setOnClickListener {
            viewModel.changeIsMovieSavedToWatchLater()
        }
    }

    private fun updateIsFavouriteImage(isFavourite: Boolean) {
        if (isFavourite) {
            binding.favouriteImageView.setImageDrawable(
                getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_favorite
                )
            )
        } else {
            binding.favouriteImageView.setImageDrawable(
                getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_favorite_border
                )
            )

        }
    }

    private fun updateIsOnWatchLaterListImage(isOnWatchLater: Boolean) {
        if (isOnWatchLater) {
            binding.archiveImageView.setImageDrawable(
                getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_archive
                )
            )
            binding.watchLaterListTextView.text = getString(R.string.add_to_watch_later_text)
        } else {
            binding.archiveImageView.setImageDrawable(
                getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_unarchive
                )
            )
            binding.watchLaterListTextView.text =
                getString(R.string.remove_from_watch_later_text)
        }
    }

}