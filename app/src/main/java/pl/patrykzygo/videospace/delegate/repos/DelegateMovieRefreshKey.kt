package pl.patrykzygo.videospace.delegate.repos

import androidx.paging.PagingState
import pl.patrykzygo.videospace.data.network.MovieResponse

interface DelegateMovieRefreshKey {
    fun refreshKey(state: PagingState<Int, MovieResponse>): Int?
}