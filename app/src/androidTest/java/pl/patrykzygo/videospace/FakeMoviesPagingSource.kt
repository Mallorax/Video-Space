package pl.patrykzygo.videospace

import androidx.paging.PagingState
import pl.patrykzygo.videospace.data.network.MovieResponse
import pl.patrykzygo.videospace.repository.MoviesPagingSource
import pl.patrykzygo.videospace.util.fakeCorrectMoviesResponseUi
import javax.inject.Inject

open class FakeMoviesPagingSource @Inject constructor() : MoviesPagingSource() {

    override fun setMoviesRequestType(moviesRequestType: String, movieId: Int) {}

    override fun getRefreshKey(state: PagingState<Int, MovieResponse>): Int? {
        return 1
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