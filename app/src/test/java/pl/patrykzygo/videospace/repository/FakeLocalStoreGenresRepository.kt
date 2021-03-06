package pl.patrykzygo.videospace.repository

import pl.patrykzygo.videospace.data.app.Genre
import pl.patrykzygo.videospace.repository.local_store.LocalStoreGenresRepository

class FakeLocalStoreGenresRepository(var genres: MutableList<Genre>) : LocalStoreGenresRepository {


    override suspend fun getGenreId(genreName: String): RepositoryResponse<Int> {
        val genresWithName = genres.filter { t -> t.genreName == genreName }
        val genreWithName = genresWithName.firstOrNull()
        return if (genreWithName != null) RepositoryResponse.success(genreWithName.genreId)
        else RepositoryResponse.error("No such genre")
    }

    override suspend fun getAllGenres(): RepositoryResponse<List<Genre>> {
        return if (genres.isNotEmpty()) RepositoryResponse.success(genres)
        else RepositoryResponse.error("test error")
    }


}