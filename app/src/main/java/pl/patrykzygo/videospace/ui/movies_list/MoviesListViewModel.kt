package pl.patrykzygo.videospace.ui.movies_list

import androidx.lifecycle.*
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.mapMoviesResponseToMovie
import pl.patrykzygo.videospace.repository.genre_paging.GenrePagingSource
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(private val repo: GenrePagingSource): ViewModel(){

    private var _genre = MutableLiveData<String>()
    val genre: LiveData<String> get() = _genre

    fun setGenre(genre: String){
        _genre.value = genre
    }

    fun getMoviesInGenre(genre: String): LiveData<PagingData<Movie>>{
        val pager = Pager(
            PagingConfig(
                pageSize = 10,
                enablePlaceholders = true,
                prefetchDistance = 5,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {repo}
        )
            .liveData
            .cachedIn(viewModelScope)
            .map { pagingData ->
                pagingData.map { mapMoviesResponseToMovie(it) }
            }
        return pager
    }

}