package pl.patrykzygo.videospace.delegate.repos

import pl.patrykzygo.videospace.data.local.GenreDao
import pl.patrykzygo.videospace.data.local.GenreEntity
import pl.patrykzygo.videospace.data.network.movie_details.GenreItem

class CacheGenresDelegateImpl : CacheGenresDelegate {

    override suspend fun cacheRemoteGenres(
        genres: List<GenreItem?>?,
        genreDao: GenreDao
    ): List<GenreEntity>? {
        val genreEntities = genres?.mapNotNull {
            if (it?.id != null && it.name != null) {
                GenreEntity(it.id, it.name)
            } else {
                null
            }
        }
        genreEntities?.let { genreDao.insertGenres(*it.toTypedArray()) }
        return genreEntities
    }
}