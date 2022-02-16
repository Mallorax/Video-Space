package pl.patrykzygo.videospace.ui.movies_list

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.mapMoviesResponseToMovie
import pl.patrykzygo.videospace.repository.MoviesPagingResultProvider
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(private val repo: MoviesPagingResultProvider) :
    ViewModel() {

    private var _requestType = MutableLiveData<String>()
    val requestType: LiveData<String> get() = _requestType

    fun setRequestType(requestType: String, id: Int = -1) {
        repo.setMoviesRequestType(requestType, id)
        _requestType.value = requestType
    }

    fun getMovies(): LiveData<PagingData<Movie>> {
        val pager = repo.providePagingResult()
            .liveData
            .cachedIn(viewModelScope)
            .map { pagingData ->
                pagingData.map {
                    mapMoviesResponseToMovie(it)
                }
            }
        return pager
    }
}