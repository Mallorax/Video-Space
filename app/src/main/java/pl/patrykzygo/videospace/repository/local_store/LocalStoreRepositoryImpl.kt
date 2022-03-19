package pl.patrykzygo.videospace.repository.local_store

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.patrykzygo.videospace.data.app.Genre
import pl.patrykzygo.videospace.data.local.GenreDao
import pl.patrykzygo.videospace.data.local.GenreEntity
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.local.MoviesDao
import pl.patrykzygo.videospace.data.mapGenreEntityToGenre
import pl.patrykzygo.videospace.data.mapGenreItemToGenreNullable
import pl.patrykzygo.videospace.data.network.movie_details.MovieDetailsResponse
import pl.patrykzygo.videospace.networking.GenresEntryPoint
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import pl.patrykzygo.videospace.repository.RepositoryResponse
import pl.patrykzygo.videospace.repository.delegate.CancellationExceptionCheck
import pl.patrykzygo.videospace.repository.delegate.CancellationExceptionCheckImpl
import javax.inject.Inject

class LocalStoreRepositoryImpl @Inject constructor(
    private val moviesDao: MoviesDao,
    private val genreDao: GenreDao,
    private val moviesEntryPoint: MoviesEntryPoint,
    private val genreEntryPoint: GenresEntryPoint
) : LocalStoreRepository,
    CancellationExceptionCheck by CancellationExceptionCheckImpl() {

    // It seems to me like this class does too much,
    // some split of responsibilities could be in order

    override suspend fun insertFavourite(movie: MovieEntity) {
        moviesDao.insertFavourite(movie)
    }

    override suspend fun insertFavourites(vararg movie: MovieEntity) {
        moviesDao.insertFavourites(*movie)
    }

    override suspend fun getAllFavourites(): RepositoryResponse<List<MovieEntity>> {
        return RepositoryResponse.success(listOf())
    }

    override suspend fun getAllMoviesWithStatus(status: String): RepositoryResponse<List<MovieEntity>> {
        return try {
            val data = moviesDao.getAllMoviesWithStatus(status)
            RepositoryResponse.success(data)
        } catch (e: Exception) {
            checkForCancellationException(e)
            RepositoryResponse.error(e.message.orEmpty())
        }
    }

    override suspend fun getAllGenres(): RepositoryResponse<List<Genre>> {
        val daoResponse = genreDao.getGenres()
        if (daoResponse.isNotEmpty()) {
            return RepositoryResponse.success(daoResponse.map { mapGenreEntityToGenre(it) })
        }
        val networkResponse = genreEntryPoint.getGenresForMovies()
        if (networkResponse.isSuccessful) {
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
        try {
            val genreResponse = genreEntryPoint.getGenresForMovies()
            return if (genreResponse.isSuccessful) {
                val genreResponseBody = genreResponse.body()?.genres
                GlobalScope.launch(Dispatchers.IO) {
                    val genreEntities = genreResponseBody?.mapNotNull {
                        if (it?.id != null && it.name != null) {
                            GenreEntity(it.id, it.name)
                        } else {
                            null
                        }
                    }
                    genreEntities?.let { genreDao.insertGenres(*it.toTypedArray()) }
                }
                val responseGenre = genreResponseBody?.firstOrNull { t -> t?.name == genreName }
                if (responseGenre != null) {
                    RepositoryResponse.success(responseGenre.id!!)
                } else {
                    RepositoryResponse.error("No such genre")
                }
            } else {
                RepositoryResponse.error(genreResponse.message())
            }

        } catch (e: Exception) {
            checkForCancellationException(e)
            return RepositoryResponse.error(e.message.orEmpty())
        }
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
            checkForCancellationException(e)
            RepositoryResponse.error(e.message ?: "")
        }

    }

}