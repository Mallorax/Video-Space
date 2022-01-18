package pl.patrykzygo.videospace.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import pl.patrykzygo.videospace.data.network.MoviesResponse
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import retrofit2.HttpException
import javax.inject.Inject

class MoviesPagingSource @Inject constructor(private val moviesEntryPoint: MoviesEntryPoint) :
    PagingSource<Int, MoviesResponse>() {

    override fun getRefreshKey(state: PagingState<Int, MoviesResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesResponse> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = moviesEntryPoint.requestPopularMovies(page = nextPageNumber)
            return if (response.isSuccessful) {
                LoadResult.Page(
                    data = response.body()!!.moviesList,
                    prevKey = previousKey(response.body()!!.page),
                    nextKey = nextKey(response.body()!!.page, response.body()!!.totalPages)
                )
            } else {
                throw HttpException(response)
            }
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: NullPointerException) {
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }

    }

    private fun previousKey(currKey: Int): Int? {
        return if (currKey == 1) {
            null
        } else {
            currKey - 1
        }
    }

    private fun nextKey(currKey: Int, lastKey: Int): Int? {
        return if (currKey == lastKey) {
            null
        } else {
            currKey + 1
        }
    }
}