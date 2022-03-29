package pl.patrykzygo.videospace.repository.movies_paging

import androidx.paging.PagingState
import pl.patrykzygo.videospace.constants.MoviesRequestType
import pl.patrykzygo.videospace.data.network.EntryPointMoviesResponse
import pl.patrykzygo.videospace.data.network.MovieResponse
import pl.patrykzygo.videospace.delegate.repos.*
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class MoviesPagingSourceImpl @Inject constructor(private val moviesEntryPoint: MoviesEntryPoint) :
    MoviesPagingSource(),
    MovieCalcKeyPositionDelegate by MovieCalcNextKeyDelegateImpl(),
    DelegateMovieRefreshKey by DelegateMovieRefreshKeyImpl(),
    CancellationExceptionCheck by CancellationExceptionCheckImpl() {

    private var moviesRequestType = MoviesRequestType.POPULAR
    var movieId = -1

    override fun setMoviesRequestType(moviesRequestType: String, movieId: Int) {
        this.moviesRequestType = moviesRequestType
        this.movieId = movieId
    }

    override fun getRefreshKey(state: PagingState<Int, MovieResponse>): Int? {
        return refreshKey(state)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResponse> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = handleRequestType(nextPageNumber, movieId)
            return if (response.isSuccessful) {
                LoadResult.Page(
                    data = response.body()!!.movieList,
                    prevKey = previousKey(response.body()!!.page),
                    nextKey = nextKey(response.body()!!.page, response.body()!!.totalPages)
                )
            } else {
                throw HttpException(response)
            }
        } catch (e: Exception) {
            checkForCancellationException(e)
            e.printStackTrace()
            return LoadResult.Error(e)
        }
    }


    private suspend fun handleRequestType(
        page: Int,
        id: Int = -1
    ): Response<EntryPointMoviesResponse> {
        return when (moviesRequestType) {
            MoviesRequestType.POPULAR -> moviesEntryPoint.requestPopularMovies(page = page)
            MoviesRequestType.TOP_RATED -> moviesEntryPoint.requestTopRatedMovies(page = page)
            MoviesRequestType.NOW_PLAYING -> moviesEntryPoint.requestNowPlayingMovies(page = page)
            MoviesRequestType.UPCOMING -> moviesEntryPoint.requestUpcomingMovies(page = page)
            MoviesRequestType.RECOMMENDATIONS -> moviesEntryPoint.requestRecommendationsForMovie(
                page = page,
                id = id
            )
            MoviesRequestType.SIMILAR -> moviesEntryPoint.requestSimilarMovies(page = page, id = id)
            else -> moviesEntryPoint.requestPopularMovies(page = page)
        }
    }


}