package pl.patrykzygo.videospace.util

import okhttp3.MediaType
import okhttp3.ResponseBody
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.local.GenreEntity
import pl.patrykzygo.videospace.data.network.EntryPointMoviesResponse
import pl.patrykzygo.videospace.data.network.MovieResponse
import pl.patrykzygo.videospace.data.network.movie_details.GenresItem
import pl.patrykzygo.videospace.data.network.movie_details.GenresResponse
import retrofit2.Response

fun fakeCorrectMoviesResponse(page: Int, numberOfPages: Int): Response<EntryPointMoviesResponse> {
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
            GenresItem("1", 1),
            GenresItem("2", 2),
            GenresItem("3", 3),
            GenresItem("4", 4),
            GenresItem("5", 5),
            GenresItem("6", 6),
            GenresItem("7", 7),
        )
    )
    return Response.success(genresResponse)
}

fun fakeGenreEntitiesList(): List<GenreEntity> {
    return listOf(
        GenreEntity(1, "1"),
        GenreEntity(2, "2"),
        GenreEntity(3, "3"),
        GenreEntity(4, "4"),
        GenreEntity(5, "5"),
        GenreEntity(6, "6"),
        GenreEntity(7, "7"),
    )
}


fun fakeMoviesResponseList(page: Int): List<MovieResponse> {
    val moviesList = mutableListOf<MovieResponse>()
    for (i in 0..10) {
        moviesList.add(
            MovieResponse(
                false, "frew",
                listOf(1, 2, 3, 4),
                page * 10 + i,
                "eng",
                "title",
                "overview",
                2.2,
                "poster",
                "releaseDate",
                "title",
                false,
                3.5,
                5
            )
        )
    }
    return moviesList
}

fun provideMovieWithId(id: Int): Movie {
    return Movie(
        false, "", listOf(), id,
        "lang", "title $id", "overview",
        0.0, "poster $id", "release date",
        "title", false, 1.00, 323
    )
}

