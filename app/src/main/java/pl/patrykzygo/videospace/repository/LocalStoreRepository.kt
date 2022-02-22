package pl.patrykzygo.videospace.repository

import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.network.MovieResponse
import pl.patrykzygo.videospace.data.network.movie_details.MovieDetailsResponse

interface LocalStoreRepository {

    suspend fun insertFavourite(movie: MovieEntity)
    suspend fun insertFavourites(vararg movie: MovieEntity)
    suspend fun getAllFavourites(): RepositoryResponse<List<MovieEntity>>
    suspend fun getSpecificMovie(id: Int): RepositoryResponse<MovieDetailsResponse>
    suspend fun getSpecificFavourite(id: Int): RepositoryResponse<MovieEntity>

}