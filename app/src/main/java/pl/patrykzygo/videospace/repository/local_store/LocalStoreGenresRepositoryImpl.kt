package pl.patrykzygo.videospace.repository.local_store

import pl.patrykzygo.videospace.data.app.Genre
import pl.patrykzygo.videospace.data.local.GenreDao
import pl.patrykzygo.videospace.data.local.GenreEntity
import pl.patrykzygo.videospace.data.mapGenreEntityToGenre
import pl.patrykzygo.videospace.data.mapGenreItemToGenreNullable
import pl.patrykzygo.videospace.data.network.movie_details.GenresItem
import pl.patrykzygo.videospace.data.network.movie_details.GenresResponse
import pl.patrykzygo.videospace.networking.GenresEntryPoint
import pl.patrykzygo.videospace.repository.RepositoryResponse
import pl.patrykzygo.videospace.repository.delegate.CancellationExceptionCheck
import pl.patrykzygo.videospace.repository.delegate.CancellationExceptionCheckImpl
import retrofit2.Response
import javax.inject.Inject

class LocalStoreGenresRepositoryImpl @Inject constructor(
    private val genreDao: GenreDao,
    private val genreEntryPoint: GenresEntryPoint
) : LocalStoreGenresRepository,
    CancellationExceptionCheck by CancellationExceptionCheckImpl() {

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