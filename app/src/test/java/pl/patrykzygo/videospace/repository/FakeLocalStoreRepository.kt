package pl.patrykzygo.videospace.repository

import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.network.MovieResponse
import pl.patrykzygo.videospace.data.network.movie_details.MovieDetailsResponse

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

    override suspend fun getSpecificMovie(id: Int): RepositoryResponse<MovieDetailsResponse> {
        return RepositoryResponse.success(getMovieDetailsResponseWithId(id))
    }

    override suspend fun getSpecificFavourite(id: Int): RepositoryResponse<MovieEntity> {
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
            "", 123, listOf(), 2.43, id, 23,
            45234543, "descritpion $id", "orginal title $id",
            34324, "", "date $id", 2.03, "",
            false, "", ""
        )
    }

}