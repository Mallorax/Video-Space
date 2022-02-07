package pl.patrykzygo.videospace.data

import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.network.MovieResponse

fun mapMoviesResponseToMovie(movieResponse: MovieResponse): Movie {
    return Movie(
        movieResponse.adult,
        movieResponse.backdropPath.orEmpty(),
        listOf(), //TODO: leaving it empty for now, should fetch actual list from room/api
        movieResponse.id,
        movieResponse.originalLanguage,
        movieResponse.originalTitle,
        movieResponse.overview,
        movieResponse.popularity,
        movieResponse.posterPath.orEmpty(),
        movieResponse.releaseDate,
        movieResponse.title,
        movieResponse.video,
        movieResponse.voteAverage,
        movieResponse.voteCount
    )
}

fun mapMovieToMovieEntity(movie: Movie): MovieEntity {
    return MovieEntity(
        movie.id,
        isFavourite = movie.isFavourite,
        isOnWatchLater = movie.isOnWatchLater
    )
}