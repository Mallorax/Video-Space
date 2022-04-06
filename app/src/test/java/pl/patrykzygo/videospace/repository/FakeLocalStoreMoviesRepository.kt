package pl.patrykzygo.videospace.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pl.patrykzygo.videospace.constants.MovieStatus
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.network.movie_details.GenreItem
import pl.patrykzygo.videospace.data.network.movie_details.MovieDetailsResponse
import pl.patrykzygo.videospace.fakeMoviesEntitiesList
import pl.patrykzygo.videospace.repository.local_store.LocalStoreMoviesRepository

open class FakeLocalStoreMoviesRepository : LocalStoreMoviesRepository {

    val movieList = fakeMoviesEntitiesList().toMutableList()

    companion object FakeRepoMessages {
        const val MOVIES_WITH_STATUS_ERROR = "movies with status error"
        const val DB_SPECIFIED_MOVIE_ERROR = "No such element"
        const val SPECIFIC_MOVIE_ERROR = "Specific movie error"
    }

    override suspend fun getAllMoviesWithStatus(status: String): Flow<RepositoryResponse<List<MovieEntity>>> {
        return if (status != MovieStatus.DROPPED) { //1 variant returns error for testing purposes
            val moviesWithStatus = movieList.filter { t -> t.status == status }
            flow { emit(RepositoryResponse.success(moviesWithStatus)) }
        } else {
            flow { emit(RepositoryResponse.error(MOVIES_WITH_STATUS_ERROR)) }
        }
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
        return if (id > 0) RepositoryResponse.success(getMovieDetailsResponseWithId(id))
        else RepositoryResponse.error(SPECIFIC_MOVIE_ERROR)
    }

    override suspend fun getSpecificMovieFromDb(id: Int): RepositoryResponse<MovieEntity> {
        val movie = movieList.find { t -> t.movieId == id }
        return if (movie != null) RepositoryResponse.success(movie)
        else RepositoryResponse.error(DB_SPECIFIED_MOVIE_ERROR)
    }

    private fun getMovieDetailsResponseWithId(id: Int): MovieDetailsResponse {
        if (movieList.any { t -> t.movieId == id }) {
            val movie = movieList.first { t -> t.movieId == id }
            return MovieDetailsResponse(
                id = movie.movieId, title = movie.title, genres = listOf(
                    GenreItem(1, "1"),
                    GenreItem(2, "2"),
                    GenreItem(3, "3")
                )
            )
        }
        return MovieDetailsResponse(
            "", "id", false, "title $id",
            "", 123, listOf(
                GenreItem(1, "1"),
                GenreItem(2, "2"),
                GenreItem(3, "3")
            ), 2.43, id, 23,
            45234543, "descritpion $id",
            "orginal title $id",
            34324, "",
            "date $id", 2.03,
            "", false, "", ""
        )
    }

}