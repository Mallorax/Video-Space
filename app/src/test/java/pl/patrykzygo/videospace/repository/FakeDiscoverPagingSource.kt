package pl.patrykzygo.videospace.repository

import androidx.paging.PagingState
import pl.patrykzygo.videospace.constants.SortOptions
import pl.patrykzygo.videospace.data.app.DiscoverMovieRequest
import pl.patrykzygo.videospace.data.network.MovieResponse
import pl.patrykzygo.videospace.delegate.repos.DelegateMovieRefreshKey
import pl.patrykzygo.videospace.delegate.repos.DelegateMovieRefreshKeyImpl
import pl.patrykzygo.videospace.delegate.repos.MovieCalcKeyPositionDelegate
import pl.patrykzygo.videospace.delegate.repos.MovieCalcNextKeyDelegateImpl
import pl.patrykzygo.videospace.fakeCorrectMoviesResponse
import pl.patrykzygo.videospace.repository.discover_paging.DiscoverPagingSource

class FakeDiscoverPagingSource : DiscoverPagingSource(),
    MovieCalcKeyPositionDelegate by MovieCalcNextKeyDelegateImpl(),
    DelegateMovieRefreshKey by DelegateMovieRefreshKeyImpl() {

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
        val currentPage = params.key ?: 1
        val response = fakeCorrectMoviesResponse(currentPage, 5)
        return LoadResult.Page(
            data = response.body()!!.movieList,
            prevKey = previousKey(response.body()!!.page),
            nextKey = nextKey(response.body()!!.page, response.body()!!.totalPages)
        )
    }
}