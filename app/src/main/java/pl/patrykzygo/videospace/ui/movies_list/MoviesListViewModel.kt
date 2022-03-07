package pl.patrykzygo.videospace.ui.movies_list

import androidx.lifecycle.*
import androidx.paging.*
import kotlinx.coroutines.runBlocking
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.mapMoviesResponseToMovie
import pl.patrykzygo.videospace.others.SortOptions
import pl.patrykzygo.videospace.repository.RepositoryResponse
import pl.patrykzygo.videospace.repository.genre_paging.GenrePagingSource
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepository

class MoviesListViewModel constructor(
    private val repo: GenrePagingSource,
    private val localRepo: LocalStoreRepository
) : ViewModel() {

    private var _genre = MutableLiveData<String>()
    val genre: LiveData<String> get() = _genre

    private var _sortOption = MutableLiveData(SortOptions.POPULARITY_DESC)
    val sortOption: LiveData<String> get() = _sortOption

    fun setSortingOption(sortOption: String){
        _sortOption.value = sortOption
    }


    fun getMoviesInGenre(genre: String): LiveData<PagingData<Movie>> {
        _genre.value = genre
        var response: RepositoryResponse<Int>?
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

    fun triggerSortByMostPopular() {
        val currentSortOption = _sortOption.value
        if (currentSortOption == SortOptions.POPULARITY_DESC){
            _sortOption.value = SortOptions.POPULARITY_ASC
            return
        }
        if (currentSortOption == SortOptions.POPULARITY_ASC){
            _sortOption.value = SortOptions.POPULARITY_DESC
            return
        }
        _sortOption.value = SortOptions.POPULARITY_DESC
    }

    fun triggerSortByReleaseDate() {
        val currentSortOption = _sortOption.value
        if (currentSortOption == SortOptions.RELEASE_DATE_DESC){
            _sortOption.value = SortOptions.RELEASE_DATE_ASC
            return
        }
        if (currentSortOption == SortOptions.RELEASE_DATE_ASC){
            _sortOption.value = SortOptions.SCORE_AVERAGE_DESC
            return
        }

        _sortOption.value = SortOptions.RELEASE_DATE_DESC
    }

    fun triggerSortByAverageScore() {
        val currentSortOption = _sortOption.value
        if (currentSortOption == SortOptions.SCORE_AVERAGE_DESC){
            _sortOption.value = SortOptions.SCORE_AVERAGE_ASC
            return
        }
        if (currentSortOption == SortOptions.SCORE_AVERAGE_ASC){
            _sortOption.value = SortOptions.SCORE_AVERAGE_DESC
            return
        }
        _sortOption.value = SortOptions.SCORE_AVERAGE_DESC
    }

    fun triggerSortByVoteCount() {
        val currentSortOption = _sortOption.value
        if (currentSortOption == SortOptions.VOTE_COUNT_DESC){
            _sortOption.value = SortOptions.VOTE_COUNT_ASC
            return
        }
        if (currentSortOption == SortOptions.VOTE_COUNT_ASC){
            _sortOption.value = SortOptions.VOTE_COUNT_DESC
            return
        }
        _sortOption.value = SortOptions.VOTE_COUNT_DESC
    }

}