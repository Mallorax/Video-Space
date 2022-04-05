package pl.patrykzygo.videospace.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.network.movie_details.MovieDetailsResponse
import pl.patrykzygo.videospace.repository.RepositoryResponse
import pl.patrykzygo.videospace.repository.local_store.LocalStoreMoviesRepository
import javax.inject.Inject

open class FakeLocalStoreMoviesRepositoryAndroid @Inject constructor() :
    LocalStoreMoviesRepository {

    private val movieList = mutableListOf<MovieEntity>()

    override suspend fun getAllMoviesWithStatus(status: String): Flow<RepositoryResponse<List<MovieEntity>>> {
        val moviesWithStatus = movieList.filter { t -> t.status == status }
        return flow { emit(RepositoryResponse.success(moviesWithStatus)) }
    }


    override suspend fun saveMovieToDb(movie: MovieEntity) {
        movieList.add(movie)
    }

    override suspend fun saveMoviesToDb(vararg movies: MovieEntity) {
        movieList.addAll(movies)
    }

    override suspend fun getAllMoviesFromDb(): Flow<RepositoryResponse<List<MovieEntity>>> {
        return flow { emit(RepositoryResponse.success(movieList)) }
    }

    override suspend fun getSpecificMovie(id: Int): RepositoryResponse<MovieDetailsResponse> {
        return RepositoryResponse.success(getMovieDetailsResponseWithId(id))
    }

    override suspend fun getSpecificMovieFromDb(id: Int): RepositoryResponse<MovieEntity> {
        val movie = movieList.find { t -> t.movieId == id }
        return if (movie != null) RepositoryResponse.success(movie)
        else RepositoryResponse.error("No such element")
    }

    private fun getMovieDetailsResponseWithId(id: Int): MovieDetailsResponse {
        return MovieDetailsResponse(
            "", "id", false, "title $id",
            "", 123, listOf(), 2.43, id, 323,
            45234543, "descritpion $id", "orginal title $id",
            34324, "", "date $id", 1.00, "",
            false, "", ""
        )
    }

}