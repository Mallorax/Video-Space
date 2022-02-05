package pl.patrykzygo.videospace.ui.movie_dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.databinding.MovieBottomSheetBinding

class MovieModalBottomSheet: BottomSheetDialogFragment() {

    private var _binding: MovieBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieBottomSheetViewModel by viewModels()

    private var isFavourite = false

    companion object {
        const val TAG = "MovieModalBottomSheet"
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MovieBottomSheetBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        val movie = arguments?.getParcelable<Movie>("movie")
        viewModel.setMovie(movie)
        observeViewModelState()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun observeViewModelState(){
        viewModel.isMovieSet.observe(viewLifecycleOwner, Observer {
            if (!it){
                dismiss()
            }
        })
    }

    private fun setListeners(){
        binding.likeTextView.setOnClickListener {
            isFavourite = if (isFavourite){
                binding.favouriteImageView.setImageDrawable(getDrawable(requireContext(), R.drawable.ic_baseline_favorite_border))
                false
            }else{
                binding.favouriteImageView.setImageDrawable(getDrawable(requireContext(), R.drawable.ic_baseline_favorite))
                true
            }
        }

        binding.moreInfoTextView.setOnClickListener {
            Log.v(TAG, "clicky click")
        }
    }

}