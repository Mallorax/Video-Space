package pl.patrykzygo.videospace.data

import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.network.MoviesResponse

fun mapMoviesResponseToMovie(moviesResponse: MoviesResponse): Movie{
    return Movie(
        moviesResponse.adult,
        moviesResponse.backdropPath.orEmpty(),
        listOf(), //TODO: leaving it empty for now, should fetch actual list from room/api
        moviesResponse.id,
        moviesResponse.originalLanguage,
        moviesResponse.originalTitle,
        moviesResponse.overview,
        moviesResponse.popularity,
        moviesResponse.posterPath.orEmpty(),
        moviesResponse.releaseDate,
        moviesResponse.title,
        moviesResponse.video,
        moviesResponse.voteAverage,
        moviesResponse.voteCount
    )
}