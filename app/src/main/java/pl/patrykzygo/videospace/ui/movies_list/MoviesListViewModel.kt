package pl.patrykzygo.videospace.ui.movies_list

import androidx.lifecycle.*
import androidx.paging.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.mapMoviesResponseToMovie
import pl.patrykzygo.videospace.others.SortOptions
import pl.patrykzygo.videospace.repository.RepositoryResponse
import pl.patrykzygo.videospace.repository.genre_paging.GenrePagingSource
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepository
import java.lang.IllegalArgumentException

class MoviesListViewModel constructor(
    private val repo: GenrePagingSource,
    private val localRepo: LocalStoreRepository
) : ViewModel() {

    private var _genre = MutableLiveData<String>()
    val genre: LiveData<String> get() = _genre

    private var _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _sortOption = MutableStateFlow(SortOptions.POPULARITY_DESC)
    val sortOption: LiveData<String> get() = _sortOption.asLiveData()


    //TODO: mutable state flow for sort options
    fun setGenre(genre: String) {
        _genre.value = genre
        getMoviesInGenre()
    }

    private fun setSortingOption(sortOption: String) {
        _sortOption.value = sortOption
        getMoviesInGenre()
    }


    fun getMoviesInGenre(): Flow<PagingData<Movie>> {
        if (_genre.value == null) throw IllegalArgumentException()
        var response: RepositoryResponse<Int>?
        //runs blocking as result of operation is needed for next steps
        runBlocking {
            response = localRepo.getGenreId(_genre.value!!)
        }
        if (response?.status == RepositoryResponse.Status.SUCCESS) {
            return _sortOption.flatMapLatest {
                repo.setParameters(genreId = response!!.data!!, sortingOption = _sortOption.value)
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
                        pagingData.filter { it.genreIds.contains(response!!.data!!) }
                    }
                    .map { pagingData ->
                        pagingData.map { mapMoviesResponseToMovie(it) }
                    }
                return@flatMapLatest pager
            }


        } else {
            throw IllegalArgumentException()
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