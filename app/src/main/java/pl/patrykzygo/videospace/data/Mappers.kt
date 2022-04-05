package pl.patrykzygo.videospace.data

import pl.patrykzygo.videospace.data.app.Genre
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.app.SimpleMovie
import pl.patrykzygo.videospace.data.local.GenreEntity
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.network.MovieResponse
import pl.patrykzygo.videospace.data.network.movie_details.GenreItem
import pl.patrykzygo.videospace.data.network.movie_details.MovieDetailsResponse

fun mapMoviesResponseToSimpleMovie(movieResponse: MovieResponse): SimpleMovie {
    return SimpleMovie(
        movieId = movieResponse.id,
        title = movieResponse.title.orEmpty(),
        releaseDate = movieResponse.releaseDate.orEmpty(),
        voteAverage = movieResponse.voteAverage ?: 0.00,
        voteCount = movieResponse.voteCount,
        posterPath = movieResponse.posterPath.orEmpty()
    )
}


fun mapMovieDetailsResponseToMovie(movieDetailsResponse: MovieDetailsResponse): Movie {
    return Movie(
        movieDetailsResponse.adult ?: false,
        movieDetailsResponse.backdropPath.orEmpty(),
        movieDetailsResponse.genres.mapNotNull { t -> t?.name },
        movieDetailsResponse.id ?: -1,
        movieDetailsResponse.originalLanguage.orEmpty(),
        movieDetailsResponse.originalTitle.orEmpty(),
        movieDetailsResponse.overview.orEmpty(),
        movieDetailsResponse.popularity ?: -1.0,
        movieDetailsResponse.posterPath.orEmpty(),
        movieDetailsResponse.releaseDate.orEmpty(),
        movieDetailsResponse.title.orEmpty(),
        movieDetailsResponse.video ?: false,
        movieDetailsResponse.voteAverage ?: -1.0,
        movieDetailsResponse.voteCount ?: -1,
    )
}

fun mapMovieToMovieEntity(movie: Movie): MovieEntity {
    return MovieEntity(
        movie.id,
        title = movie.title,
        score = movie.score,
        status = movie.status,
        releaseDate = movie.releaseDate,
        posterPath = movie.posterPath
    )
}

fun mapGenreEntityToGenre(genreEntity: GenreEntity): Genre {
    return Genre(
        genreEntity.id,
        genreEntity.name
    )
}

fun mapMovieEntityToSimpleMovie(movieEntity: MovieEntity): SimpleMovie {
    return SimpleMovie(
        movieEntity.movieId,
        movieEntity.title,
        movieEntity.releaseDate,
        userScore = movieEntity.score,
        status = movieEntity.status,
        posterPath = movieEntity.posterPath
    )
}


fun mapGenreItemToGenreNullable(genreItem: GenreItem?): Genre? {
    return if (genreItem?.id != null && genreItem.name != null) {
        Genre(genreItem.id, genreItem.name)
    } else {
        null
    }
}