package pl.patrykzygo.videospace.ui.movie_dialogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.mapMovieToMovieEntity
import pl.patrykzygo.videospace.repository.RepositoryResponse
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepository
import pl.patrykzygo.videospace.ui.DispatchersProvider

class MovieBottomSheetViewModel
constructor(
    private val repository: LocalStoreRepository,
    private val dispatchers: DispatchersProvider
) : ViewModel() {


    private val _movie = MutableLiveData<Movie?>()
    val movie: LiveData<Movie?> get() = _movie


    fun setMovie(movie: Movie?) {
        if (movie != null) {
            runBlocking {
                movie.isFavourite = checkIfIsFavourite(movie.id)
                movie.isOnWatchLater = checkIfIsOnWatchLater(movie.id)
            }
            this._movie.value = movie
        } else {
            this._movie.value = null
        }
    }

    fun changeIsMovieLiked() {
        val movie = _movie.value
        if (movie != null) {
            movie.isFavourite = !movie.isFavourite
            _movie.value = movie
            viewModelScope.launch(dispatchers.io) {
                repository.insertFavourite(mapMovieToMovieEntity(movie))
                delay(100)
            }
        }
    }

    fun changeIsMovieSavedToWatchLater() {
        val movie = _movie.value
        if (movie != null) {
            movie.isOnWatchLater = !movie.isOnWatchLater
            _movie.value = movie
            viewModelScope.launch(dispatchers.io) {
                repository.insertFavourite(mapMovieToMovieEntity(movie))
            }
        }
    }

    private suspend fun checkIfIsFavourite(id: Int): Boolean {
        var isFavourite = false
        val response = repository.getSpecificFavourite(id)
        if (response.status == RepositoryResponse.Status.SUCCESS) {
            isFavourite = response.data!!.isFavourite
        }
        return isFavourite
    }

    private suspend fun checkIfIsOnWatchLater(id: Int): Boolean {
        var isOnWatchLater = false
        val response = repository.getSpecificFavourite(id)
        if (response.status == RepositoryResponse.Status.SUCCESS) {
            isOnWatchLater = response.data!!.isOnWatchLater
        }
        return isOnWatchLater
    }


}