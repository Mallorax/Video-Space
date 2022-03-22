package pl.patrykzygo.videospace.ui.save_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.repository.local_store.LocalStoreMoviesRepository
import pl.patrykzygo.videospace.ui.delegate.HandleMovieStatusDelegate
import pl.patrykzygo.videospace.ui.delegate.HandleMovieStatusDelegateImpl
import pl.patrykzygo.videospace.ui.dispatchers.DispatchersProvider
import javax.inject.Inject

@HiltViewModel
class SaveMovieViewModel @Inject constructor(
    private val moviesRepo: LocalStoreMoviesRepository,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel(),
    HandleMovieStatusDelegate by HandleMovieStatusDelegateImpl() {

    private val _inputFeedbackMessage = MutableLiveData<String>()
    val inputFeedbackMessage: LiveData<String> get() = _inputFeedbackMessage

    companion object FeedbackMessages {
        const val SAVE_SUCCESSFUL_MSG = "Movie saved successfully"
        const val SAVE_WRONG_STATUS = "You have to select 1 status"
        const val SAVE_WRONG_UNRECOGNISABLE_MOVIE = "Couldn't recognise the movie"
        const val SAVE_WRONG_INCORRECT_SCORE = "Incorrect score"
    }


    fun saveMovie(id: Int?, title: String?, status: String?, score: Int?) {
        if (!checkMovieInput(id, title, status, score)) {
            return
        }
        val movieStatus: String
        try {
            movieStatus = handleMovieStatus(status)
        } catch (e: IllegalArgumentException) {
            _inputFeedbackMessage.value = SAVE_WRONG_STATUS
            return
        }
        viewModelScope.launch(dispatchersProvider.io) {
            moviesRepo.saveMovieToDb(
                MovieEntity(
                    id!!,
                    title = title!!,
                    score = score!!,
                    status = movieStatus
                )
            )
            _inputFeedbackMessage.postValue(SAVE_SUCCESSFUL_MSG)
        }
    }


    private fun checkMovieInput(id: Int?, title: String?, status: String?, score: Int?): Boolean {
        if (id == null || title == null) {
            _inputFeedbackMessage.value = SAVE_WRONG_UNRECOGNISABLE_MOVIE
            return false
        }
        if (score == null || score < 1 || score > 10) {
            _inputFeedbackMessage.value = SAVE_WRONG_INCORRECT_SCORE
            return false
        }
        if (status == null || status.isEmpty()) {
            _inputFeedbackMessage.value = SAVE_WRONG_STATUS
            return false
        }
        return true
    }
}