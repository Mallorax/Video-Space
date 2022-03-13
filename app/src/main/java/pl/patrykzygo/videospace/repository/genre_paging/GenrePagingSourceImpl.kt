package pl.patrykzygo.videospace.repository.genre_paging

import androidx.paging.PagingState
import pl.patrykzygo.videospace.data.app.DiscoverMovieRequest
import pl.patrykzygo.videospace.data.network.MovieResponse
import pl.patrykzygo.videospace.networking.DiscoverEntryPoint
import pl.patrykzygo.videospace.others.SortOptions
import pl.patrykzygo.videospace.repository.delegate.DelegateMovieRefreshKey
import pl.patrykzygo.videospace.repository.delegate.DelegateMovieRefreshKeyImpl
import pl.patrykzygo.videospace.repository.delegate.MovieCalcKeyPositionDelegate
import pl.patrykzygo.videospace.repository.delegate.MovieCalcNextKeyDelegateImpl
import retrofit2.HttpException
import javax.inject.Inject

class GenrePagingSourceImpl @Inject constructor(private val entryPoint: DiscoverEntryPoint) :
    GenrePagingSource(),
    MovieCalcKeyPositionDelegate by MovieCalcNextKeyDelegateImpl(),
    DelegateMovieRefreshKey by DelegateMovieRefreshKeyImpl() {

    private var request: DiscoverMovieRequest? = null
    private var sortOption: String = SortOptions.POPULARITY_DESC

    //Id of a genre is needed to fetch movies from entry point
    override fun setRequest(request: DiscoverMovieRequest) {
        this.request = request
    }

    override fun setSortingOption(sortingOption: String) {
        this.sortOption = sortingOption
    }

    override fun getRefreshKey(state: PagingState<Int, MovieResponse>): Int? {
        return refreshKey(state)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResponse> {
        try {
            val currentPage = params.key ?: 1
            val response = entryPoint.getMoviesWithGenre(
                page = currentPage,
                sortOptions = sortOption,
                includedGenres = request?.includedGenres,
                excludedGenres = request?.excludedGenres,
                minScore = request?.minScore,
                minVoteCount = request?.minVoteCount
            )
            return if (response.isSuccessful) {
                LoadResult.Page(
                    data = response.body()!!.movieList,
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

}