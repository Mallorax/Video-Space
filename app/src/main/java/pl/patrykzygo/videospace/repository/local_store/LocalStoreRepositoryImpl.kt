package pl.patrykzygo.videospace.repository.local_store

import pl.patrykzygo.videospace.data.app.Genre
import pl.patrykzygo.videospace.data.local.GenreDao
import pl.patrykzygo.videospace.data.local.GenreEntity
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.local.MoviesDao
import pl.patrykzygo.videospace.data.mapGenreEntityToGenre
import pl.patrykzygo.videospace.data.mapGenreItemToGenreNullable
import pl.patrykzygo.videospace.data.network.movie_details.GenresItem
import pl.patrykzygo.videospace.data.network.movie_details.GenresResponse
import pl.patrykzygo.videospace.data.network.movie_details.MovieDetailsResponse
import pl.patrykzygo.videospace.networking.GenresEntryPoint
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import pl.patrykzygo.videospace.repository.RepositoryResponse
import pl.patrykzygo.videospace.repository.delegate.CancellationExceptionCheck
import pl.patrykzygo.videospace.repository.delegate.CancellationExceptionCheckImpl
import retrofit2.Response
import javax.inject.Inject

class LocalStoreRepositoryImpl @Inject constructor(
    private val moviesDao: MoviesDao,
    private val genreDao: GenreDao,
    private val moviesEntryPoint: MoviesEntryPoint,
    private val genreEntryPoint: GenresEntryPoint
) : LocalStoreRepository,
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

    //returns genres from room db, if there aren't any inside room db
    // fetches genres from API and uses room db as local cache
    override suspend fun getAllGenres(): RepositoryResponse<List<Genre>> {
        val daoResponse = genreDao.getGenres()
        if (daoResponse.isNotEmpty()) {
            return RepositoryResponse.success(daoResponse.map { mapGenreEntityToGenre(it) })
        }
        val networkResponse = genreEntryPoint.getGenresForMovies()
        if (networkResponse.isSuccessful) {
            cacheRemoteGenres(networkResponse.body()?.genres)
            val data = networkResponse.body()?.genres
                ?: return RepositoryResponse.error("Couldn't retrieve genres")
            return RepositoryResponse.success(data.mapNotNull { mapGenreItemToGenreNullable(it) })
        }
        return RepositoryResponse.error("Couldn't retrieve genres")
    }

    override suspend fun getGenreId(genreName: String): RepositoryResponse<Int> {
        val daoResponse = genreDao.getGenres()
        val daoGenre = daoResponse.firstOrNull { t -> t.name == genreName }
        if (daoGenre != null) {
            return RepositoryResponse.success(daoGenre.id)
        }
        return try {
            val genresResponse = genreEntryPoint.getGenresForMovies()
            cacheRemoteGenres(genresResponse.body()?.genres)
            handleGenresNetworkResponse(genresResponse, genreName)
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

    //saves genres from api into room db
    private suspend fun cacheRemoteGenres(genres: List<GenresItem?>?) {
        val genreEntities = genres?.mapNotNull {
            if (it?.id != null && it.name != null) {
                GenreEntity(it.id, it.name)
            } else {
                null
            }
        }
        genreEntities?.let { genreDao.insertGenres(*it.toTypedArray()) }
    }

    private fun handleGenresNetworkResponse(
        genresResponse: Response<GenresResponse>,
        genreName: String
    ): RepositoryResponse<Int> {
        return if (genresResponse.isSuccessful) {
            val genreResponseBody = genresResponse.body()?.genres
            val responseGenre = genreResponseBody?.firstOrNull { t -> t?.name == genreName }
            if (responseGenre != null) {
                RepositoryResponse.success(responseGenre.id!!)
            } else {
                RepositoryResponse.error("No such genre")
            }
        } else {
            RepositoryResponse.error(genresResponse.message())
        }
    }


}