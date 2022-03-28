package pl.patrykzygo.videospace.ui.movie_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.patrykzygo.videospace.data.app.DiscoverMovieRequest
import pl.patrykzygo.videospace.data.app.Genre
import pl.patrykzygo.videospace.repository.RepositoryResponse
import pl.patrykzygo.videospace.repository.local_store.LocalStoreGenresRepository
import pl.patrykzygo.videospace.ui.dispatchers.DispatchersProvider
import javax.inject.Inject

@HiltViewModel
class SearchMovieViewModel @Inject constructor(
    private val genresRepo: LocalStoreGenresRepository,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val _genres = MutableLiveData<List<Genre>>()
    val genres: LiveData<List<Genre>> get() = _genres

    private val _dataRequestErrorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _dataRequestErrorMessage

    private val _submitRequestInputErrorMessage = MutableLiveData<String>()
    val submitRequestInputErrorMessage: LiveData<String> get() = _submitRequestInputErrorMessage

    private val _requestMoviesLiveEvent = LiveEvent<DiscoverMovieRequest>()
    val requestMoviesLiveEvent get() = _requestMoviesLiveEvent

    private val _includedGenres = mutableListOf<Genre>()
    private val _excludedGenres = mutableListOf<Genre>()

    //used for testing only
    val includedGenres: List<Genre> get() = _includedGenres
    val excludedGenres: List<Genre> get() = _excludedGenres

    companion object Messages {
        const val MIN_SCORE_ERROR_MSG = "Something wrong with minimum score"
        const val VOTE_COUNT_ERROR_MSG = "Vote count has to be a number"
        const val GENRES_OVERLAP_ERROR_MSG = "Genres can't overlap"
    }


    fun getAllGenre() {
        viewModelScope.launch(dispatchersProvider.io) {
            val repoResponse = genresRepo.getAllGenres()
            if (repoResponse.status == RepositoryResponse.Status.SUCCESS) {
                _genres.postValue(repoResponse.data!!)
            } else {
                _dataRequestErrorMessage.postValue(repoResponse.message!!)
            }
        }
    }

    fun addIncludedGenre(genreName: String) {
        val genre = _genres.value?.first { t -> t.genreName == genreName }
        genre?.let {
            _includedGenres.add(genre)
        }
    }

    fun removeIncludedGenre(genreName: String) {
        val genre = _genres.value?.first { t -> t.genreName == genreName }
        _includedGenres.remove(genre)
    }

    fun addExcludedGenre(genreName: String) {
        val genre = _genres.value?.first { t -> t.genreName == genreName }
        genre?.let {
            _excludedGenres.add(genre)
        }
    }

    fun removeExcludedGenre(genreName: String) {
        val genre = _genres.value?.first { t -> t.genreName == genreName }
        _excludedGenres.remove(genre)
    }


    fun submitRequest(minScoreInput: Int?, minVotesInput: String) {
        val minimumScore: Int
        if (minScoreInput == null) {
            _submitRequestInputErrorMessage.value = MIN_SCORE_ERROR_MSG
            return
        } else {
            minimumScore = minScoreInput
        }
        val minVotesInt: Int? = try {
            minVotesInput.toInt()
        } catch (e: NumberFormatException) {
            if (minVotesInput.isNotEmpty()) {
                _submitRequestInputErrorMessage.value = VOTE_COUNT_ERROR_MSG
                return
            } else {
                null
            }
        }
        if (checkIfGenresAreOverlapping()) return
        val request = DiscoverMovieRequest(
            _includedGenres.map { t -> t.genreId }.joinToString(separator = ","),
            _excludedGenres.map { t -> t.genreId }.joinToString(separator = ","),
            minimumScore, minVotesInt
        )

        _requestMoviesLiveEvent.value = request
    }

    private fun checkIfGenresAreOverlapping(): Boolean {
        val areOverlapping = _includedGenres.any { it in excludedGenres }
        if (areOverlapping) {
            _submitRequestInputErrorMessage.value = GENRES_OVERLAP_ERROR_MSG
        }
        return areOverlapping
    }


}