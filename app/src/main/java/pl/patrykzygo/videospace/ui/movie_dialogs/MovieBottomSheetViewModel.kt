package pl.patrykzygo.videospace.ui.movie_dialogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.mapMovieToMovieEntity
import pl.patrykzygo.videospace.repository.LocalStoreRepository
import pl.patrykzygo.videospace.repository.RepositoryResponse
import javax.inject.Inject

@HiltViewModel
class MovieBottomSheetViewModel
@Inject constructor(private val repository: LocalStoreRepository) : ViewModel() {

    private val _isMovieSet = MutableLiveData<Boolean>()
    val isMovieSet: LiveData<Boolean> get() = _isMovieSet

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> get() = _movie


    fun setMovie(movie: Movie?) {
        if (movie != null) {
            runBlocking {
                val favouriteResponse = checkIfIsFavourite(movie)
                movie.isFavourite = favouriteResponse.isFavourite
                movie.isOnWatchLater = favouriteResponse.isOnWatchLater
            }
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
            viewModelScope.launch(Dispatchers.IO){
                repository.insertFavourite(mapMovieToMovieEntity(movie))
            }
        }
    }

    fun changeIsMovieSavedToWatchLater() {
        val movie = _movie.value
        if (movie != null) {
            movie.isOnWatchLater = !movie.isOnWatchLater
            _movie.value = movie!!
            viewModelScope.launch(Dispatchers.IO){
                repository.insertFavourite(mapMovieToMovieEntity(movie))
            }
        }
    }

    private suspend fun checkIfIsFavourite(movie: Movie): MovieEntity {
        val response = repository.getSpecificFavourite(movie.id)
        if (response.status == RepositoryResponse.Status.SUCCESS) {
            if (response.data != null) {
                return response.data!!
            } else {
                return MovieEntity(-1, false, isOnWatchLater = false)
            }
        } else {
            return MovieEntity(-1, false, isOnWatchLater = false)
        }

    }


}