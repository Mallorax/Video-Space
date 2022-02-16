package pl.patrykzygo.videospace.repository

import androidx.paging.PagingSource
import pl.patrykzygo.videospace.data.network.MovieResponse

abstract class MoviesPagingSource : PagingSource<Int, MovieResponse>() {

    abstract fun setMoviesRequestType(moviesRequestType: String, movieId: Int)

}