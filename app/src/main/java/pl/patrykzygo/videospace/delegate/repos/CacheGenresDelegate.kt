package pl.patrykzygo.videospace.delegate.repos

import pl.patrykzygo.videospace.data.local.GenreDao
import pl.patrykzygo.videospace.data.local.GenreEntity
import pl.patrykzygo.videospace.data.network.movie_details.GenreItem

interface CacheGenresDelegate {

    suspend fun cacheRemoteGenres(genres: List<GenreItem?>?, genreDao: GenreDao): List<GenreEntity>?
}