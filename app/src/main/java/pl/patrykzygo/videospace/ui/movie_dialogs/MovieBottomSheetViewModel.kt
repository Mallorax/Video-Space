package pl.patrykzygo.videospace.ui.movie_dialogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.patrykzygo.videospace.data.app.Movie
import javax.inject.Inject

@HiltViewModel
class MovieBottomSheetViewModel @Inject constructor() : ViewModel() {

    private val _isMovieSet = MutableLiveData<Boolean>()
    val isMovieSet: LiveData<Boolean> get() = _isMovieSet

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> get() = _movie


    fun setMovie(movie: Movie?) {
        if (movie != null) {
            this._movie.value = movie
        } else {
            _isMovieSet.value = false
        }
    }

    fun changeIsMovieLiked() {
        val movie = _movie.value
        if (movie != null) {
            movie.isFavourite = !movie.isFavourite
            _movie.value = movie!!
        }
    }

    fun changeIsMovieSavedToWatchLater() {
        val movie = _movie.value
        if (movie != null) {
            movie.isOnWatchLater = !movie.isOnWatchLater
            _movie.value = movie!!
        }
    }


}