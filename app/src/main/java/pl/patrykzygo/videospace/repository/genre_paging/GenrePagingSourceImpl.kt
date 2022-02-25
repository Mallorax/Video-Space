package pl.patrykzygo.videospace.repository.genre_paging

import androidx.paging.PagingState
import pl.patrykzygo.videospace.data.network.MovieResponse
import pl.patrykzygo.videospace.networking.DiscoverEntryPoint
import retrofit2.HttpException
import javax.inject.Inject

//TODO: There is some temporary code duplication between here and MoviesPagingSource, don't forget to handle it
//meant to provide movies of specific genre
class GenrePagingSourceImpl @Inject constructor(private val entryPoint: DiscoverEntryPoint) :
    GenrePagingSource() {

    private var genreId: String = ""

    override fun setGenre(genreId: Int) {
        this.genreId = genreId.toString()
    }

    override fun getRefreshKey(state: PagingState<Int, MovieResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResponse> {
        try {
            val currentPage = params.key ?: 1
            val response = entryPoint.getMoviesWithGenre(
                includedGenres = genreId,
                page = currentPage
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