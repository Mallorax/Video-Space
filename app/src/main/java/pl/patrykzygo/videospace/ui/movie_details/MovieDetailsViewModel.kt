package pl.patrykzygo.videospace.ui.movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.mapMovieDetailsResponseToMovie
import pl.patrykzygo.videospace.repository.LocalStoreRepository
import pl.patrykzygo.videospace.repository.RepositoryResponse
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(private val repo: LocalStoreRepository) : ViewModel() {

    private val _movie = MutableLiveData<Movie?>()
    val movie: LiveData<Movie?> get() = _movie

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage


    fun setMovie(movie: Movie?) {
        if (movie != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val response = repo.getSpecificMovie(movie.id)
                if (response.status == RepositoryResponse.Status.SUCCESS) {
                    _movie.postValue(mapMovieDetailsResponseToMovie(response.data!!))
                } else if (response.status == RepositoryResponse.Status.ERROR) {
                    _errorMessage.postValue(response.message!!)
                }
            }
        }
    }

    fun toggleFavourite(){
        val movie = _movie.value
        if (movie != null){
            movie.isFavourite = !movie.isFavourite
            _movie.value = movie
        }

    }

}