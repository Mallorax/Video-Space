package pl.patrykzygo.videospace

import pl.patrykzygo.videospace.data.app.Movie

fun provideMovieWithIdUi(id: Int): Movie {
    return Movie(
        false, "", listOf(), id,
        "lang", "title $id", "overview",
        0.0, "poster $id", "release date",
        "title", false, 1.00, 323
    )
}