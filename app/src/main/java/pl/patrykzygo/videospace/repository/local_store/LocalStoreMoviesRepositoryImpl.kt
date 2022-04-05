package pl.patrykzygo.videospace.repository.local_store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.local.MoviesDao
import pl.patrykzygo.videospace.data.network.movie_details.MovieDetailsResponse
import pl.patrykzygo.videospace.delegate.repos.CancellationExceptionCheck
import pl.patrykzygo.videospace.delegate.repos.CancellationExceptionCheckImpl
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import pl.patrykzygo.videospace.repository.RepositoryResponse
import javax.inject.Inject

class LocalStoreMoviesRepositoryImpl @Inject constructor(
    private val moviesDao: MoviesDao,
    private val moviesEntryPoint: MoviesEntryPoint,
) : LocalStoreMoviesRepository,
    CancellationExceptionCheck by CancellationExceptionCheckImpl() {

    override suspend fun saveMovieToDb(movie: MovieEntity) {
        moviesDao.insertMovie(movie)
    }

    override suspend fun saveMoviesToDb(vararg movies: MovieEntity) {
        moviesDao.insertMovies(*movies)
    }

    override suspend fun getAllMoviesFromDb(): Flow<RepositoryResponse<List<MovieEntity>>> {
        return try {
            val dbResponse = moviesDao.getAllMovies()
            return dbResponse.map { t -> RepositoryResponse.success(t) }
        } catch (e: Exception) {
            checkForCancellationException(e)
            e.printStackTrace()
            flow { emit(RepositoryResponse.error(e.message.orEmpty())) }
        }
    }

    //returns all movies saved into room db with a given status
    override suspend fun getAllMoviesWithStatus(status: String): Flow<RepositoryResponse<List<MovieEntity>>> {
        return try {
            val dbResponse = moviesDao.getAllMoviesWithStatus(status)
            return dbResponse.map { t -> RepositoryResponse.success(t) }
        } catch (e: Exception) {
            checkForCancellationException(e)
            e.printStackTrace()
            flow { emit(RepositoryResponse.error(e.message.orEmpty())) }
        }
    }


    override suspend fun getSpecificMovie(id: Int): RepositoryResponse<MovieDetailsResponse> {
        return try {
            val response = moviesEntryPoint.requestMovie(id = id)
            if (response.isSuccessful) RepositoryResponse.success(response.body()!!)
            else RepositoryResponse.error(response.message())
        } catch (e: Exception) {
            checkForCancellationException(e)
            e.printStackTrace()
            RepositoryResponse.error(e.message.orEmpty())
        }
    }

    override suspend fun getSpecificMovieFromDb(id: Int): RepositoryResponse<MovieEntity> {
        return try {
            val movie = moviesDao.getMovieWithId(id)
            if (movie == null) RepositoryResponse.error("No such movie")
            else RepositoryResponse.success(movie)
        } catch (e: Exception) {
            checkForCancellationException(e)
            e.printStackTrace()
            RepositoryResponse.error(e.message ?: "")
        }
    }


}