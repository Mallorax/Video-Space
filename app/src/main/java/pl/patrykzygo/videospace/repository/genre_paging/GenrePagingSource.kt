package pl.patrykzygo.videospace.repository.genre_paging

import androidx.paging.PagingSource
import pl.patrykzygo.videospace.data.network.MovieResponse

//abstraction that is meant to make testing easier
abstract class GenrePagingSource(): PagingSource<Int, MovieResponse>() {

    abstract fun setGenre(genreId: Int)
}