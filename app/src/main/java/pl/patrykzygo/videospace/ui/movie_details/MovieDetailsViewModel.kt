package pl.patrykzygo.videospace.ui.movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.mapMovieDetailsResponseToMovie
import pl.patrykzygo.videospace.data.mapMovieToMovieEntity
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepository
import pl.patrykzygo.videospace.repository.RepositoryResponse


class MovieDetailsViewModel constructor(
    private val repo: LocalStoreRepository
) : ViewModel() {

    private val _movie = MutableLiveData<Movie?>()
    val movie: LiveData<Movie?> get() = _movie

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _genres = LiveEvent<List<String>>()
    val genres: LiveData<List<String>> get() = _genres

    fun setMovie(movie: Movie?) {
        if (movie != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val response = repo.getSpecificMovie(movie.id)
                val isFavourite = repo.getSpecificFavourite(movie.id).data?.isFavourite
                if (response.status == RepositoryResponse.Status.SUCCESS) {
                    val mappedMovie = mapMovieDetailsResponseToMovie(response.data!!)
                    mappedMovie.isFavourite = isFavourite ?: false
                    _movie.postValue(mappedMovie)
                    _genres.postValue(mappedMovie.genres)
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
            viewModelScope.launch(Dispatchers.IO){
                repo.insertFavourite(mapMovieToMovieEntity(movie))
            }
        }

    }

}