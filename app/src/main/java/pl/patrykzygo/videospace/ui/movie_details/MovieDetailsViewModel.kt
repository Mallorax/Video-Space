package pl.patrykzygo.videospace.ui.movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.patrykzygo.videospace.data.app.DiscoverMovieRequest
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.mapMovieDetailsResponseToMovie
import pl.patrykzygo.videospace.repository.RepositoryResponse
import pl.patrykzygo.videospace.repository.local_store.LocalStoreGenresRepository
import pl.patrykzygo.videospace.repository.local_store.LocalStoreMoviesRepository
import pl.patrykzygo.videospace.ui.dispatchers.DispatchersProvider
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val moviesRepo: LocalStoreMoviesRepository,
    private val genresRepo: LocalStoreGenresRepository,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val _movie = MutableLiveData<Movie?>()
    val movie: LiveData<Movie?> get() = _movie

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _genres = LiveEvent<List<String>>()
    val genres: LiveData<List<String>> get() = _genres

    private val _saveMovieEvent = LiveEvent<Movie>()
    val saveMovieEvent: LiveData<Movie> get() = _saveMovieEvent

    private val _searchInGenreLiveEvent = LiveEvent<DiscoverMovieRequest>()
    val searchInGenreLiveEvent get() = _searchInGenreLiveEvent

    private val _searchInGenreErrorMessage = MutableLiveData<String>()
    val searchInGenreErrorMessage get() = _searchInGenreErrorMessage


    //Because of they way API movie passed into fragment of this viewmodel doesn't contain genres it belongs to
    //thus additional call to api is needed to get missing data
    fun setMovie(movieId: Int?) {
        if (movieId != null) {
            viewModelScope.launch(dispatchersProvider.io) {
                val response = moviesRepo.getSpecificMovie(movieId)
                if (response.status == RepositoryResponse.Status.SUCCESS) {
                    val mappedMovie = mapMovieDetailsResponseToMovie(response.data!!)
                    _movie.postValue(mappedMovie)
                    _genres.postValue(mappedMovie.genres)
                } else if (response.status == RepositoryResponse.Status.ERROR) {
                    _errorMessage.postValue(response.message!!)
                }
            }
        }
    }

    fun moveToGenreList(searchedGenre: String) {
        viewModelScope.launch(dispatchersProvider.io) {
            val response = genresRepo.getAllGenres()
            if (response.status == RepositoryResponse.Status.SUCCESS) {
                val genre = response.data!!.first { t -> t.genreName == searchedGenre }
                val request = DiscoverMovieRequest(includedGenres = genre.genreId.toString())
                _searchInGenreLiveEvent.postValue(request)
            } else {
                _searchInGenreErrorMessage.postValue("Check your internet connection")
            }
        }
    }

    fun launchSaveMovieEvent() {
        _saveMovieEvent.value = _movie.value
    }


}