package pl.patrykzygo.videospace.ui.movies_list

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import pl.patrykzygo.videospace.constants.SortOptions
import pl.patrykzygo.videospace.data.app.DiscoverMovieRequest
import pl.patrykzygo.videospace.data.mapMoviesResponseToMovie
import pl.patrykzygo.videospace.repository.discover_paging.DiscoverPagingSource
import javax.inject.Inject

//Not sure if it's good idea to use it in VM,
// however I couldn't find other way to make it work the way I wanted
@ExperimentalCoroutinesApi
@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val repo: DiscoverPagingSource,
    private val state: SavedStateHandle
) : ViewModel() {

    private var _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    // I have no clue how I've managed to make it work, so don't even ask me
    private val _sortOption =
        MutableStateFlow(state.get("sortOption") ?: SortOptions.POPULARITY_DESC)
    val sortOption: LiveData<String> get() = _sortOption.asLiveData()

    var movies = _sortOption.flatMapLatest {
        createMoviesPagingData()
    }.stateIn(
        scope = viewModelScope + Dispatchers.IO,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = state.get("sortOption")
    )


    private fun createMoviesPagingData() =
        Pager(
            PagingConfig(
                pageSize = 10,
                enablePlaceholders = true,
                prefetchDistance = 5,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { repo }
        )
            .flow
            .map { pagingData ->
                pagingData.map { mapMoviesResponseToMovie(it) }
            }
            .cachedIn(viewModelScope)


    fun setRequest(movieRequest: DiscoverMovieRequest) {
        repo.setRequest(movieRequest)
    }

    private fun setSortingOption(sortOption: String) {
        state.set("sortOption", sortOption)
        repo.setSortingOption(sortOption)
        _sortOption.value = sortOption
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