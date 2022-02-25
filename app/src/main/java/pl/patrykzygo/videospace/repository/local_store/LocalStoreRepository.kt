package pl.patrykzygo.videospace.repository.local_store

import pl.patrykzygo.videospace.data.app.Genre
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.network.movie_details.MovieDetailsResponse
import pl.patrykzygo.videospace.repository.RepositoryResponse

interface LocalStoreRepository {

    suspend fun getGenreId(genreName: String): RepositoryResponse<Int>
    suspend fun insertFavourite(movie: MovieEntity)
    suspend fun insertFavourites(vararg movie: MovieEntity)
    suspend fun getAllFavourites(): RepositoryResponse<List<MovieEntity>>
    suspend fun getSpecificMovie(id: Int): RepositoryResponse<MovieDetailsResponse>
    suspend fun getSpecificFavourite(id: Int): RepositoryResponse<MovieEntity>

}