package pl.patrykzygo.videospace.data

import pl.patrykzygo.videospace.data.app.Genre
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.local.GenreEntity
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.network.MovieResponse
import pl.patrykzygo.videospace.data.network.movie_details.GenreItem
import pl.patrykzygo.videospace.data.network.movie_details.MovieDetailsResponse

fun mapMoviesResponseToMovie(movieResponse: MovieResponse): Movie {
    return Movie(
        movieResponse.adult ?: false,
        movieResponse.backdropPath.orEmpty(),
        listOf(),
        movieResponse.id,
        movieResponse.originalLanguage.orEmpty(),
        movieResponse.originalTitle.orEmpty(),
        movieResponse.overview.orEmpty(),
        movieResponse.popularity ?: 0.0,
        movieResponse.posterPath.orEmpty(),
        movieResponse.releaseDate.orEmpty(),
        movieResponse.title.orEmpty(),
        movieResponse.video ?: false,
        movieResponse.voteAverage ?: 0.0,
        movieResponse.voteCount
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
        status = movie.status
    )
}

fun mapGenreEntityToGenre(genreEntity: GenreEntity): Genre {
    return Genre(
        genreEntity.id,
        genreEntity.name
    )
}

fun mapGenreItemToGenreNullable(genreItem: GenreItem?): Genre? {
    return if (genreItem?.id != null && genreItem.name != null) {
        Genre(genreItem.id, genreItem.name)
    } else {
        null
    }
}