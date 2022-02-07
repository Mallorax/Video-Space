package pl.patrykzygo.videospace.repository

import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.network.MovieResponse

interface LocalStoreRepository {

    suspend fun insertFavourite(movie: MovieEntity)
    suspend fun insertFavourites(vararg movie: MovieEntity)
    suspend fun getAllFavourites(): RepositoryResponse<List<MovieEntity>>
    suspend fun getSpecificMovie(id: Int): RepositoryResponse<MovieResponse>
    suspend fun getSpecificFavourite(id: Int): RepositoryResponse<MovieEntity>

}