package pl.patrykzygo.videospace.repository.local_store

import pl.patrykzygo.videospace.data.app.Genre
import pl.patrykzygo.videospace.repository.RepositoryResponse

interface LocalStoreGenresRepository {

    suspend fun getGenreId(genreName: String): RepositoryResponse<Int>
    suspend fun getAllGenres(): RepositoryResponse<List<Genre>>
}