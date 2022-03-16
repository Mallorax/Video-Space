package pl.patrykzygo.videospace.ui.stored_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.mapMovieDetailsResponseToMovie
import pl.patrykzygo.videospace.others.MovieStatus
import pl.patrykzygo.videospace.repository.RepositoryResponse
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepository
import pl.patrykzygo.videospace.ui.dispatchers.DispatchersProvider

class StoredListViewModel(
    private val repo: LocalStoreRepository,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val _moviesStatus = MutableLiveData(MovieStatus.UNASSIGNED)
    val movieStatus: LiveData<String> get() = _moviesStatus

    private val _movies = MutableLiveData<List<Movie>>(listOf())
    val movies: LiveData<List<Movie>> get() = _movies

    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String> get() = _errorMessage

    fun getMoviesWithStatus(status: String) {
        _moviesStatus.value = status
        getMovies()
    }

    private fun getMovies() {
        val status = _moviesStatus.value ?: return
        viewModelScope.launch(dispatchersProvider.io) {
            val repoResponse = repo.getAllMoviesWithStatus(status)
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
                _errorMessage.postValue(repoResponse.message)
            }
        }
    }

    private suspend fun getMovie(id: Int): Movie? {
        val movie = repo.getSpecificMovie(id)
        return if (movie.status == RepositoryResponse.Status.SUCCESS) {
            mapMovieDetailsResponseToMovie(movie.data!!)
        } else {
            null
        }
    }
}