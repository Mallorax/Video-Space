package pl.patrykzygo.videospace.util

import okhttp3.MediaType
import okhttp3.ResponseBody
import pl.patrykzygo.videospace.data.app.Genre
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.local.GenreEntity
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.network.EntryPointMoviesResponse
import pl.patrykzygo.videospace.data.network.MovieResponse
import pl.patrykzygo.videospace.data.network.movie_details.GenreItem
import pl.patrykzygo.videospace.data.network.movie_details.GenresResponse
import pl.patrykzygo.videospace.data.network.movie_details.MovieDetailsResponse
import retrofit2.Response

fun fakeCorrectMoviesResponse(
    page: Int,
    numberOfPages: Int = 1
): Response<EntryPointMoviesResponse> {
    val moviesResponse = EntryPointMoviesResponse(
        page = page,
        fakeMoviesResponseList(page),
        34234,
        numberOfPages
    )
    return Response.success(moviesResponse)
}

fun fakeHttpErrorResponse(): Response<Any> {
    return Response.error(
        404,
        ResponseBody.create(
            MediaType.parse("application/json"),
            "{\"key\":[\"somestuff\"]}"
        )
    )
}

fun fakeCorrectGenresResponse(): Response<GenresResponse> {
    val genresResponse = GenresResponse(
        listOf(
            GenreItem(1, "1"),
            GenreItem(2, "2"),
            GenreItem(3, "3"),
            GenreItem(4, "4"),
            GenreItem(5, "5"),
            GenreItem(6, "6"),
            GenreItem(7, "7")
        )
    )
    return Response.success(genresResponse)
}

fun fakeGenreList(): List<Genre> {
    return listOf(
        Genre(1, "1"),
        Genre(2, "2"),
        Genre(3, "3"),
        Genre(4, "4"),
        Genre(5, "5"),
        Genre(6, "6"),
        Genre(7, "7")
    )

}

fun fakeGenreEntitiesList(): List<GenreEntity> {
    return listOf(
        GenreEntity(1, "1"),
        GenreEntity(2, "2"),
        GenreEntity(3, "3"),
        GenreEntity(4, "4"),
        GenreEntity(5, "5"),
        GenreEntity(6, "6"),
        GenreEntity(7, "7")
    )
}

fun fakeMoviesEntitiesList(): List<MovieEntity> {
    return listOf(
        MovieEntity(1, "1", releaseDate = ""),
        MovieEntity(2, "2", releaseDate = ""),
        MovieEntity(3, "3", releaseDate = ""),
        MovieEntity(4, "4", releaseDate = ""),
        MovieEntity(5, "5", releaseDate = ""),
        MovieEntity(6, "6", releaseDate = ""),
        MovieEntity(7, "7", releaseDate = "")
    )
}

fun fakeMovieDetailsResponse(id: Int): Response<MovieDetailsResponse> {
    val fakeMovieDetails = MovieDetailsResponse(
        id = id,
        title = id.toString()
    )
    return Response.success(fakeMovieDetails)
}


fun fakeMoviesResponseList(page: Int): List<MovieResponse> {
    val moviesList = mutableListOf<MovieResponse>()
    for (i in 0..10) {
        moviesList.add(
            MovieResponse(
                false, page + i,
                "poster",
                "release date",
                "title",
                5.23,
                534,
            )
        )
    }
    return moviesList
}

fun getMovieWithId(id: Int): Movie {
    return Movie(
        false, "", listOf("1", "2", "3"), id,
        "lang", "title $id", "overview",
        0.0, "poster $id", "release date",
        "title", false, 1.00, 323
    )
}

