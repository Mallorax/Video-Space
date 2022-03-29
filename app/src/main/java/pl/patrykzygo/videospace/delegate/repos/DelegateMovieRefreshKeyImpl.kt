package pl.patrykzygo.videospace.delegate.repos

import androidx.paging.PagingState
import pl.patrykzygo.videospace.data.network.MovieResponse

class DelegateMovieRefreshKeyImpl : DelegateMovieRefreshKey {

    override fun refreshKey(state: PagingState<Int, MovieResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}