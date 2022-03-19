package pl.patrykzygo.videospace.repository.local_store

import pl.patrykzygo.videospace.data.local.GenreDao
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.local.MoviesDao
import pl.patrykzygo.videospace.data.network.movie_details.MovieDetailsResponse
import pl.patrykzygo.videospace.networking.GenresEntryPoint
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import pl.patrykzygo.videospace.repository.RepositoryResponse
import pl.patrykzygo.videospace.repository.delegate.CancellationExceptionCheck
import pl.patrykzygo.videospace.repository.delegate.CancellationExceptionCheckImpl
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

    override suspend fun getAllMoviesFromDb(): RepositoryResponse<List<MovieEntity>> {
        return try {
            val dbResponse = moviesDao.getAllMovies()
            if (dbResponse.isNotEmpty()) RepositoryResponse.success(dbResponse)
            else RepositoryResponse.error("No movies saved to show")
        } catch (e: Exception) {
            checkForCancellationException(e)
            e.printStackTrace()
            RepositoryResponse.error(e.message.orEmpty())
        }
    }

    //returns all movies saved into room db with a given status
    override suspend fun getAllMoviesWithStatus(status: String): RepositoryResponse<List<MovieEntity>> {
        return try {
            val data = moviesDao.getAllMoviesWithStatus(status)
            RepositoryResponse.success(data)
        } catch (e: Exception) {
            checkForCancellationException(e)
            RepositoryResponse.error(e.message.orEmpty())
        }
    }


    override suspend fun getSpecificMovie(id: Int): RepositoryResponse<MovieDetailsResponse> {
        val response = moviesEntryPoint.requestMovie(id = id)
        return if (response.isSuccessful) RepositoryResponse.success(response.body()!!)
        else RepositoryResponse.error(response.message())
    }

    override suspend fun getSpecificMovieFromDb(id: Int): RepositoryResponse<MovieEntity> {
        return try {
            val movie = moviesDao.getMovieWithId(id)
            if (movie == null) RepositoryResponse.error("Mo such element")
            else RepositoryResponse.success(movie)
        } catch (e: Exception) {
            checkForCancellationException(e)
            RepositoryResponse.error(e.message ?: "")
        }
    }



}