package pl.patrykzygo.videospace.ui.movies_list

import androidx.lifecycle.*
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import pl.patrykzygo.videospace.data.app.DiscoverMovieRequest
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.mapMoviesResponseToMovie
import pl.patrykzygo.videospace.others.SortOptions
import pl.patrykzygo.videospace.repository.genre_paging.GenrePagingSource
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepository

class MoviesListViewModel constructor(
    private val repo: GenrePagingSource
) : ViewModel() {

    private var _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _sortOption = MutableStateFlow(SortOptions.POPULARITY_DESC)
    val sortOption: LiveData<String> get() = _sortOption.asLiveData()


    //TODO: mutable state flow for sort options
    fun setRequest(request: DiscoverMovieRequest) {
        repo.setParameters(request)
        getMoviesInGenre()
    }

    private fun setSortingOption(sortOption: String) {
        _sortOption.value = sortOption
    }


    fun getMoviesInGenre(): Flow<PagingData<Movie>> {
        return _sortOption.flatMapLatest {
            val pager = Pager(
                PagingConfig(
                    pageSize = 10,
                    enablePlaceholders = true,
                    prefetchDistance = 5,
                    initialLoadSize = 10
                ),
                pagingSourceFactory = { repo }
            )
                .flow
                .cachedIn(viewModelScope)
                //seems like API doesn't filter genres correctly, so doing it here is necessary
                .map { pagingData ->
                    pagingData.map { mapMoviesResponseToMovie(it) }
                }
            return@flatMapLatest pager
        }
    }

    fun triggerSortByMostPopular() {
        val currentSortOption = _sortOption.value
        if (currentSortOption == SortOptions.POPULARITY_DESC) {
            setSortingOption(SortOptions.POPULARITY_ASC)
            return
        }
        if (currentSortOption == SortOptions.POPULARITY_ASC) {
            setSortingOption(SortOptions.POPULARITY_DESC)
            return
        }
        setSortingOption(SortOptions.POPULARITY_DESC)
    }

    fun triggerSortByReleaseDate() {
        val currentSortOption = _sortOption.value
        if (currentSortOption == SortOptions.RELEASE_DATE_DESC) {
            setSortingOption(SortOptions.RELEASE_DATE_ASC)
            return
        }
        if (currentSortOption == SortOptions.RELEASE_DATE_ASC) {
            setSortingOption(SortOptions.RELEASE_DATE_DESC)
            return
        }
        setSortingOption(SortOptions.RELEASE_DATE_DESC)
    }

    fun triggerSortByAverageScore() {
        val currentSortOption = _sortOption.value
        if (currentSortOption == SortOptions.SCORE_AVERAGE_DESC) {
            setSortingOption(SortOptions.SCORE_AVERAGE_ASC)
            return
        }
        if (currentSortOption == SortOptions.SCORE_AVERAGE_ASC) {
            setSortingOption(SortOptions.SCORE_AVERAGE_DESC)
            return
        }
        setSortingOption(SortOptions.SCORE_AVERAGE_DESC)
    }

    fun triggerSortByVoteCount() {
        val currentSortOption = _sortOption.value
        if (currentSortOption == SortOptions.VOTE_COUNT_DESC) {
            setSortingOption(SortOptions.VOTE_COUNT_ASC)
            return
        }
        if (currentSortOption == SortOptions.VOTE_COUNT_ASC) {
            setSortingOption(SortOptions.VOTE_COUNT_DESC)
            return
        }
        setSortingOption(SortOptions.VOTE_COUNT_DESC)
    }

}