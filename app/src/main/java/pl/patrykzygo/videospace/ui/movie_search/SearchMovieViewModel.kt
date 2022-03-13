package pl.patrykzygo.videospace.ui.movie_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.launch
import pl.patrykzygo.videospace.data.app.DiscoverMovieRequest
import pl.patrykzygo.videospace.data.app.Genre
import pl.patrykzygo.videospace.repository.RepositoryResponse
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepository
import pl.patrykzygo.videospace.ui.DispatchersProvider

class SearchMovieViewModel constructor(
    private val repo: LocalStoreRepository,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val _genres = MutableLiveData<List<Genre>>()
    val genres: LiveData<List<Genre>> get() = _genres

    private val _dataRequestErrorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _dataRequestErrorMessage

    private val _voteCountErrorMessage = MutableLiveData<String>()
    val voteCountErrorMessage: LiveData<String> get() = _voteCountErrorMessage

    private val _requestMoviesLiveEvent = LiveEvent<DiscoverMovieRequest>()
    val requestMoviesLiveEvent get() = _requestMoviesLiveEvent

    private val includedGenres = mutableListOf<Genre>()
    private val excludedGenres = mutableListOf<Genre>()


    fun getAllGenres() {
        viewModelScope.launch(dispatchersProvider.io) {
            val repoResponse = repo.getAllGenres()
            if (repoResponse.status == RepositoryResponse.Status.SUCCESS) {
                _genres.postValue(repoResponse.data!!)
            } else {
                _dataRequestErrorMessage.postValue(repoResponse.message!!)
            }
        }
    }

    fun addIncludedGenres(genreName: String) {
        val genre = _genres.value?.first { t -> t.genreName == genreName }
        if (genre != null) {
            includedGenres.add(genre)
        }
    }

    fun removeIncludedGenres(genreName: String) {
        val genre = _genres.value?.first { t -> t.genreName == genreName }
        includedGenres.remove(genre)
    }

    fun addExcludedGenres(genreName: String) {
        val genre = _genres.value?.first { t -> t.genreName == genreName }
        if (genre != null) {
            excludedGenres.add(genre)
        }
    }

    fun removeExcludedGenres(genreName: String) {
        val genre = _genres.value?.first { t -> t.genreName == genreName }
        excludedGenres.remove(genre)
    }

    fun submitRequest(minScore: Int?, minVotes: String) {
        val minVotesInt: Int? = try {
            minVotes.toInt()
        } catch (e: NumberFormatException) {
            null
        }
        val request = DiscoverMovieRequest(
            includedGenres.map { t -> t.genreId }.joinToString(separator = ","),
            excludedGenres.map { t -> t.genreId }.joinToString(separator = ","),
            minScore, minVotesInt
        )

        _requestMoviesLiveEvent.value = request
    }


}