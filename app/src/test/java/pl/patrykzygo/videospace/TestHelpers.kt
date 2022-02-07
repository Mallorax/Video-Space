package pl.patrykzygo.videospace

import okhttp3.MediaType
import okhttp3.ResponseBody
import pl.patrykzygo.videospace.data.network.MovieResponse
import pl.patrykzygo.videospace.data.network.EntryPointMoviesResponse
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

fun fakeHttpErrorResponse(): Response<EntryPointMoviesResponse> {
    return Response.error(
        404,
        ResponseBody.create(
            MediaType.parse("application/json"),
            "{\"key\":[\"somestuff\"]}"
        )
    )
}

private fun fakeMoviesResponseList(page: Int): List<MovieResponse> {
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
