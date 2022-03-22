package pl.patrykzygo.videospace.fakes

import androidx.paging.PagingState
import pl.patrykzygo.videospace.data.app.DiscoverMovieRequest
import pl.patrykzygo.videospace.data.network.MovieResponse
import pl.patrykzygo.videospace.repository.discover_paging.DiscoverPagingSource
import pl.patrykzygo.videospace.util.fakeCorrectMoviesResponseUi

class FakeDiscoverPagingSourceAndroid : DiscoverPagingSource() {


    override fun setRequest(request: DiscoverMovieRequest) {
    }

    override fun setSortingOption(sortingOption: String) {
    }

    override fun getRefreshKey(state: PagingState<Int, MovieResponse>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResponse> {
        val fakeResponse = fakeCorrectMoviesResponseUi(params.key ?: 1, 2)
        return LoadResult.Page(
            data = listOf(fakeResponse).flatMap { it.body()!!.movieList },
            prevKey = params.key?.minus(1),
            nextKey = params.key?.plus(1),
        )
    }
}