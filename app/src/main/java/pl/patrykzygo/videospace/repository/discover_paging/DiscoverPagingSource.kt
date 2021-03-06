package pl.patrykzygo.videospace.repository.discover_paging

import androidx.paging.PagingSource
import pl.patrykzygo.videospace.constants.SortOptions
import pl.patrykzygo.videospace.data.app.DiscoverMovieRequest
import pl.patrykzygo.videospace.data.network.MovieResponse

//abstraction that is meant to make testing easier
abstract class DiscoverPagingSource() : PagingSource<Int, MovieResponse>() {

    abstract fun setRequest(request: DiscoverMovieRequest)
    abstract fun setSortingOption(sortingOption: String = SortOptions.POPULARITY_DESC)
}