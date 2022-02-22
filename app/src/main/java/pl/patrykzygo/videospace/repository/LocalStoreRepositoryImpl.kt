package pl.patrykzygo.videospace.repository

import pl.patrykzygo.videospace.data.local.GenreDao
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.local.MoviesDao
import pl.patrykzygo.videospace.data.network.MovieResponse
import pl.patrykzygo.videospace.data.network.movie_details.MovieDetailsResponse
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import javax.inject.Inject

class LocalStoreRepositoryImpl @Inject constructor(
    private val moviesDao: MoviesDao,
    private val genreDao: GenreDao,
    private val moviesEntryPoint: MoviesEntryPoint
) : LocalStoreRepository {

    override suspend fun insertFavourite(movie: MovieEntity) {
        moviesDao.insertFavourite(movie)
    }

    override suspend fun insertFavourites(vararg movie: MovieEntity) {
        moviesDao.insertFavourites(*movie)
    }

    override suspend fun getAllFavourites(): RepositoryResponse<List<MovieEntity>> {
        return RepositoryResponse.success(listOf())
    }

    override suspend fun getSpecificMovie(id: Int): RepositoryResponse<MovieDetailsResponse> {
        val response = moviesEntryPoint.requestMovie(id = id)
        return if (response.isSuccessful) {
            RepositoryResponse.success(response.body()!!)
        } else {
            RepositoryResponse.error(response.message())
        }
    }

    override suspend fun getSpecificFavourite(id: Int): RepositoryResponse<MovieEntity> {
        return try {
            val movie = moviesDao.getFavourite(id)
            if (movie == null) {
                RepositoryResponse.error("Mo such element")
            } else {
                RepositoryResponse.success(movie)
            }

        } catch (e: Exception) {
            RepositoryResponse.error(e.message ?: "")
        }

    }
}