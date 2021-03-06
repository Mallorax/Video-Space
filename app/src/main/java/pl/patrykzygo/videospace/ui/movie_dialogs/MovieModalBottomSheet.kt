package pl.patrykzygo.videospace.ui.movie_dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.data.app.SimpleMovie
import pl.patrykzygo.videospace.databinding.MovieBottomSheetBinding

@AndroidEntryPoint
open class MovieModalBottomSheet : BottomSheetDialogFragment() {

    private var _binding: MovieBottomSheetBinding? = null
    private val binding get() = _binding!!


    companion object {
        const val TAG = "MovieModalBottomSheet"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MovieBottomSheetBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        val movie = arguments?.getParcelable<SimpleMovie>("movie")
        binding.movie = movie

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie = arguments?.getParcelable<SimpleMovie>("movie")
        setListeners(movie)
    }


    private fun setListeners(movie: SimpleMovie?) {
        binding.moreInfoTextView.setOnClickListener {
            movie?.let {
                val bundle = Bundle()
                bundle.putInt("movie", movie.movieId)
                parentFragmentManager.setFragmentResult("movieResult", bundle)
                dismiss()
            }
        }
    }


}