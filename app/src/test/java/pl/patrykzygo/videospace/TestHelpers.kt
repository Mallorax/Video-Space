package pl.patrykzygo.videospace

import pl.patrykzygo.videospace.data.network.MoviesResponse
import pl.patrykzygo.videospace.data.network.PopularMoviesResponse
import retrofit2.Response

fun stubbedCorrectMoviesResponse(page: Int, numberOfPages: Int): Response<PopularMoviesResponse> {
    val moviesResponse = PopularMoviesResponse(
        page = page,
        fakeMoviesResponseList(page),
        34234,
        numberOfPages
    )
    return Response.success(moviesResponse)
}

private fun fakeMoviesResponseList(page: Int): List<MoviesResponse> {
    val moviesList = mutableListOf<MoviesResponse>()
    for (i in 0..10) {
        moviesList.add(
            MoviesResponse(
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
