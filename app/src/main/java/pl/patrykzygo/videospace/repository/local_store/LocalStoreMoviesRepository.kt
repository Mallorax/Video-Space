package pl.patrykzygo.videospace.repository.local_store

import pl.patrykzygo.videospace.data.app.Genre
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.network.movie_details.MovieDetailsResponse
import pl.patrykzygo.videospace.repository.RepositoryResponse

interface LocalStoreMoviesRepository {

    suspend fun saveMovieToDb(movie: MovieEntity)
    suspend fun saveMoviesToDb(vararg movies: MovieEntity)
    suspend fun getAllMoviesFromDb(): RepositoryResponse<List<MovieEntity>>
    suspend fun getSpecificMovie(id: Int): RepositoryResponse<MovieDetailsResponse>
    suspend fun getSpecificMovieFromDb(id: Int): RepositoryResponse<MovieEntity>
    suspend fun getAllMoviesWithStatus(status: String): RepositoryResponse<List<MovieEntity>>

}