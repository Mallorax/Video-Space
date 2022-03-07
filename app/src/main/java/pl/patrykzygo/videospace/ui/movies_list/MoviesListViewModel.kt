package pl.patrykzygo.videospace.ui.movies_list

import androidx.lifecycle.*
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.mapMoviesResponseToMovie
import pl.patrykzygo.videospace.repository.RepositoryResponse
import pl.patrykzygo.videospace.repository.genre_paging.GenrePagingSource
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepository
import java.lang.Exception
import javax.inject.Inject

class MoviesListViewModel constructor(
    private val repo: GenrePagingSource,
    private val localRepo: LocalStoreRepository
) : ViewModel() {

    private var _genre = MutableLiveData<String>()
    val genre: LiveData<String> get() = _genre


    fun getMoviesInGenre(genre: String): LiveData<PagingData<Movie>> {
        _genre.value = genre
        var response: RepositoryResponse<Int>? = null
        runBlocking {
            response = localRepo.getGenreId(genre)
        }
        if (response?.status == RepositoryResponse.Status.SUCCESS) {
            repo.setGenre(genreId = response!!.data!!)
            val pager = Pager(
                PagingConfig(
                    pageSize = 10,
                    enablePlaceholders = true,
                    prefetchDistance = 5,
                    initialLoadSize = 10
                ),
                pagingSourceFactory = { repo }
            )
                .liveData
                .cachedIn(viewModelScope)
                    //seems like API doesn't filter genres correctly, so doing it here is necessary
                .map { pagingData ->
                    pagingData.filter { it.genreIds.contains(response!!.data!!)}
                }
                .map { pagingData ->
                    pagingData.map { mapMoviesResponseToMovie(it) }
                }
            return pager
        } else {
            throw Exception(response?.message)
        }
    }

}