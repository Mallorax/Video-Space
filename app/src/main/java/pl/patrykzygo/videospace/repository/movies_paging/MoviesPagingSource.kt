package pl.patrykzygo.videospace.repository.movies_paging

import androidx.paging.PagingSource
import pl.patrykzygo.videospace.data.network.MovieResponse

abstract class MoviesPagingSource : PagingSource<Int, MovieResponse>() {

    abstract fun setMoviesRequestType(moviesRequestType: String, movieId: Int)

}