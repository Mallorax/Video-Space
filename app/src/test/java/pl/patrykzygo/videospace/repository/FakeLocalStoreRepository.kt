package pl.patrykzygo.videospace.repository

import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.network.MovieResponse

open class FakeLocalStoreRepository : LocalStoreRepository {

    private val movieList = mutableListOf<MovieEntity>()

    override suspend fun insertFavourite(movie: MovieEntity) {
        movieList.add(movie)
    }

    override suspend fun insertFavourites(vararg movie: MovieEntity) {
        movieList.addAll(movie)
    }

    override suspend fun getAllFavourites(): RepositoryResponse<List<MovieEntity>> {
        return RepositoryResponse.success(movieList)
    }

    override suspend fun getSpecificMovie(id: Int): RepositoryResponse<MovieResponse> {
        return RepositoryResponse.success(getMovieResponseWithId(id))
    }

    override suspend fun getSpecificFavourite(id: Int): RepositoryResponse<MovieEntity> {
        val favourite = movieList.find { t -> t.movieId == id }
        if (favourite != null) {
            return RepositoryResponse.success(favourite)
        } else {
            return RepositoryResponse.error("No such element")
        }
    }

    private fun getMovieResponseWithId(id: Int): MovieResponse {
        return MovieResponse(
            false, null, listOf(), id,
            "lang", "title $id", "overview",
            0.0, null, "release date",
            "title", false, 1.00, 323
        )
    }

}