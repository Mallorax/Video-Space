package pl.patrykzygo.videospace.repository.discover_paging

import androidx.paging.PagingState
import pl.patrykzygo.videospace.constants.SortOptions
import pl.patrykzygo.videospace.data.app.DiscoverMovieRequest
import pl.patrykzygo.videospace.data.network.MovieResponse
import pl.patrykzygo.videospace.delegate.repos.*
import pl.patrykzygo.videospace.networking.DiscoverEntryPoint
import retrofit2.HttpException
import javax.inject.Inject

class DiscoverPagingSourceImpl @Inject constructor(private val entryPoint: DiscoverEntryPoint) :
    DiscoverPagingSource(),
    MovieCalcKeyPositionDelegate by MovieCalcNextKeyDelegateImpl(),
    DelegateMovieRefreshKey by DelegateMovieRefreshKeyImpl(),
    CancellationExceptionCheck by CancellationExceptionCheckImpl() {

    private var request: DiscoverMovieRequest? = null
    private var sortOption: String = SortOptions.POPULARITY_DESC

    override fun setRequest(request: DiscoverMovieRequest) {
        this.request = request
    }

    override fun setSortingOption(sortingOption: String) {
        this.sortOption = sortingOption
    }

    override fun getRefreshKey(state: PagingState<Int, MovieResponse>): Int? {
        return refreshKey(state)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResponse> {
        try {
            val currentPage = params.key ?: 1
            val response = entryPoint.requestMoviesWithParameters(
                page = currentPage,
                sortOptions = sortOption,
                includedGenres = request?.includedGenres,
                excludedGenres = request?.excludedGenres,
                minScore = request?.minScore,
                minVoteCount = request?.minVoteCount
            )
            return if (response.isSuccessful) {
                LoadResult.Page(
                    data = response.body()!!.movieList,
                    prevKey = previousKey(response.body()!!.page),
                    nextKey = nextKey(response.body()!!.page, response.body()!!.totalPages)
                )
            } else {
                throw HttpException(response)
            }
        } catch (e: Exception) {
            checkForCancellationException(e)
            e.printStackTrace()
            return LoadResult.Error(e)
        }
    }

}