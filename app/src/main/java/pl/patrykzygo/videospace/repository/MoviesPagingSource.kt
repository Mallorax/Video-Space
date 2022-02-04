package pl.patrykzygo.videospace.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import pl.patrykzygo.videospace.data.network.EntryPointMoviesResponse
import pl.patrykzygo.videospace.data.network.MoviesResponse
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class MoviesPagingSource @Inject constructor(private val moviesEntryPoint: MoviesEntryPoint) :
    PagingSource<Int, MoviesResponse>() {

    private var moviesRequestType = MoviesRequestType.POPULAR
    var movieId = -1

    fun setMoviesRequestType(moviesRequestType: MoviesRequestType){
        this.moviesRequestType = moviesRequestType
    }

    override fun getRefreshKey(state: PagingState<Int, MoviesResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesResponse> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = handleRequestType(nextPageNumber, movieId)
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
            e.printStackTrace()
            return LoadResult.Error(e)
        } catch (e: NullPointerException) {
            e.printStackTrace()
            return LoadResult.Error(e)
        } catch (e: Exception) {
            e.printStackTrace()
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

    private suspend fun handleRequestType(page: Int, id: Int = -1): Response<EntryPointMoviesResponse>{
        return when(moviesRequestType){
            MoviesRequestType.POPULAR -> moviesEntryPoint.requestPopularMovies(page = page)
            MoviesRequestType.TOP_RATED -> moviesEntryPoint.requestTopRatedMovies(page = page)
            MoviesRequestType.NOW_PLAYING -> moviesEntryPoint.requestNowPlayingMovies(page = page)
            MoviesRequestType.UPCOMING -> moviesEntryPoint.requestUpcomingMovies(page = page)
            MoviesRequestType.RECOMMENDATIONS -> moviesEntryPoint.requestRecommendationsForMovie(page = page, id = id)
            MoviesRequestType.SIMILAR -> moviesEntryPoint.requestSimilarMovies(page = page, id = id)
        }
    }
}