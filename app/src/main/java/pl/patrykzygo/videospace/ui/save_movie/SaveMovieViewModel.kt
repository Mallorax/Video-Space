package pl.patrykzygo.videospace.ui.save_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.others.MovieStatus
import pl.patrykzygo.videospace.repository.local_store.LocalStoreMoviesRepository
import javax.inject.Inject

@HiltViewModel
class SaveMovieViewModel @Inject constructor(private val moviesRepo: LocalStoreMoviesRepository) : ViewModel() {

    private val _inputFeedbackMessage = MutableLiveData<String>()
    val inputErrorMessage: LiveData<String> get() = _inputFeedbackMessage


    fun saveMovie(id: Int?, title: String?, status: String?, score: Int?) {
        if (!checkMovieInput(id, title, status, score)) {
            return
        }
        val movieStatus: String
        try {
            movieStatus = handleMovieStatus(status)
        } catch (e: IllegalArgumentException) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            moviesRepo.saveMovieToDb(
                MovieEntity(
                    id!!,
                    title = title!!,
                    score = score!!,
                    status = movieStatus
                )
            )
            _inputFeedbackMessage.postValue("Movie saved successfully")
        }
    }

    private fun handleMovieStatus(status: String?): String {
        return when (status) {
            "Watching" -> MovieStatus.WATCHING
            "Completed" -> MovieStatus.COMPLETED
            "Plan to Watch" -> MovieStatus.PLAN_TO_WATCH
            "On Hold" -> MovieStatus.ON_HOLD
            "Dropped" -> MovieStatus.DROPPED
            else -> {
                _inputFeedbackMessage.value = "You have to select 1 status"
                throw IllegalArgumentException()
            }
        }
    }

    private fun checkMovieInput(id: Int?, title: String?, status: String?, score: Int?): Boolean {
        if (id == null || title == null) {
            _inputFeedbackMessage.value = "Couldn't recognise the movie"
            return false
        }
        if (score == null || score < 1 || score > 10) {
            _inputFeedbackMessage.value = "Incorrect score"
            return false
        }
        if (status == null || status.isEmpty()) {
            _inputFeedbackMessage.value = "You have to select 1 status"
            return false
        }
        return true
    }
}