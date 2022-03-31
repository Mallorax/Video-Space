package pl.patrykzygo.videospace.ui.stored_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import pl.patrykzygo.videospace.constants.MovieStatus
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.mapMovieDetailsResponseToMovie
import pl.patrykzygo.videospace.repository.RepositoryResponse
import pl.patrykzygo.videospace.repository.local_store.LocalStoreMoviesRepository
import pl.patrykzygo.videospace.delegate.ui.HandleMovieStatusDelegate
import pl.patrykzygo.videospace.delegate.ui.HandleMovieStatusDelegateImpl
import pl.patrykzygo.videospace.ui.dispatchers.DispatchersProvider
import javax.inject.Inject

@HiltViewModel
class StoredListViewModel @Inject constructor(
    private val moviesRepo: LocalStoreMoviesRepository,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel(),
    HandleMovieStatusDelegate by HandleMovieStatusDelegateImpl() {

    private val _moviesStatus = MutableLiveData(MovieStatus.UNASSIGNED)
    val movieStatus: LiveData<String> get() = _moviesStatus

    private val _movies = MutableLiveData<List<Movie>>(listOf())
    val movies: LiveData<List<Movie>> get() = _movies

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    companion object Messages {
        const val WRONG_STATUS_ERROR = "Status is incorrect"
    }

    fun getMoviesWithStatus(status: String) {
        try {
            val handledStatus = handleMovieStatus(status)
            _moviesStatus.value = handledStatus
            getMovies()
        } catch (e: IllegalArgumentException) {
            _errorMessage.value = WRONG_STATUS_ERROR
            return
        }
    }

    private fun getMovies() {
        val status = _moviesStatus.value ?: return
        viewModelScope.launch(dispatchersProvider.io) {
            val repoResponse = moviesRepo.getAllMoviesWithStatus(status)
            if (repoResponse.status == RepositoryResponse.Status.SUCCESS) {
                val dbMovies = repoResponse.data!!
                val movies = mutableListOf<Deferred<Movie?>>()
                dbMovies.forEach { movieEntity ->
                    val movie = async {
                        getMovie(movieEntity.movieId)
                    }
                    movies.add(movie)
                }
                _movies.postValue(movies.awaitAll().filterNotNull())
            } else {
                _errorMessage.postValue(repoResponse.message!!)
            }
        }
    }

    private suspend fun getMovie(id: Int): Movie? {
        val movie = moviesRepo.getSpecificMovie(id)
        return if (movie.status == RepositoryResponse.Status.SUCCESS) {
            mapMovieDetailsResponseToMovie(movie.data!!)
        } else {
            null
        }
    }
}