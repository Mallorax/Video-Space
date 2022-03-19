package pl.patrykzygo.videospace.fakes

import pl.patrykzygo.videospace.data.app.Genre
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.network.movie_details.MovieDetailsResponse
import pl.patrykzygo.videospace.repository.RepositoryResponse
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepository
import javax.inject.Inject

open class FakeLocalStoreRepositoryAndroid @Inject constructor() : LocalStoreRepository {

    private val movieList = mutableListOf<MovieEntity>()

    override suspend fun getAllGenres(): RepositoryResponse<List<Genre>> {
        return RepositoryResponse.success(
            listOf(
                Genre(1, "1"),
                Genre(2, "2"),
                Genre(3, "3"),
                Genre(4, "4"),
                Genre(5, "5"),
                Genre(6, "6"),
                Genre(7, "7"),
                Genre(8, "8"),
            )
        )
    }

    override suspend fun getAllMoviesWithStatus(status: String): RepositoryResponse<List<MovieEntity>> {
        val moviesWithStatus = movieList.filter { t -> t.status == status }
        return RepositoryResponse.success(moviesWithStatus)
    }

    override suspend fun getGenreId(genreName: String): RepositoryResponse<Int> {
        return RepositoryResponse.success(1)
    }

    override suspend fun saveMovieToDb(movie: MovieEntity) {
        movieList.add(movie)
    }

    override suspend fun saveMoviesToDb(vararg movies: MovieEntity) {
        movieList.addAll(movies)
    }

    override suspend fun getAllMoviesFromDb(): RepositoryResponse<List<MovieEntity>> {
        return RepositoryResponse.success(movieList)
    }

    override suspend fun getSpecificMovie(id: Int): RepositoryResponse<MovieDetailsResponse> {
        return RepositoryResponse.success(getMovieDetailsResponseWithId(id))
    }

    override suspend fun getSpecificMovieFromDb(id: Int): RepositoryResponse<MovieEntity> {
        val favourite = movieList.find { t -> t.movieId == id }
        if (favourite != null) {
            return RepositoryResponse.success(favourite)
        } else {
            return RepositoryResponse.error("No such element")
        }
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