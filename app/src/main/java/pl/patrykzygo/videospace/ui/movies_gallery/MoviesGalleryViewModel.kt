package pl.patrykzygo.videospace.ui.movies_gallery

import androidx.lifecycle.*
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.patrykzygo.videospace.data.app.SimpleMovie
import pl.patrykzygo.videospace.data.mapMoviesResponseToSimpleMovie
import pl.patrykzygo.videospace.repository.movies_paging.MoviesPagingSource
import javax.inject.Inject

@HiltViewModel
class MoviesGalleryViewModel @Inject constructor(private val moviesRepo: MoviesPagingSource) :
    ViewModel() {

    private var _requestType = MutableLiveData<String>()
    val requestType: LiveData<String> get() = _requestType

    fun setRequestType(requestType: String, id: Int = -1) {
        moviesRepo.setMoviesRequestType(requestType, id)
        _requestType.value = requestType
    }

    fun getMovies(): LiveData<PagingData<SimpleMovie>> {
        val pager = Pager(
            PagingConfig(
                pageSize = 10,
                enablePlaceholders = true,
                prefetchDistance = 5,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { moviesRepo }
        )
            .liveData
            .cachedIn(viewModelScope)
            .map { pagingData ->
                pagingData.map {
                    mapMoviesResponseToSimpleMovie(it)
                }
            }
        return pager
    }
}