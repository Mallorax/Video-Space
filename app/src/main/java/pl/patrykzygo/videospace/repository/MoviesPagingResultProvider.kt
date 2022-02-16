package pl.patrykzygo.videospace.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import pl.patrykzygo.videospace.data.network.MovieResponse

interface MoviesPagingResultProvider {

    fun providePagingResult(
        config: PagingConfig = PagingConfig(
            pageSize = 10,
            enablePlaceholders = true,
            prefetchDistance = 5,
            initialLoadSize = 10
        )
    ): Pager<Int, MovieResponse>

    fun setMoviesRequestType(moviesRequestType: String, movieId: Int = -1)

}