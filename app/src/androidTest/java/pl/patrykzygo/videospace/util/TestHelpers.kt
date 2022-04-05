package pl.patrykzygo.videospace.util

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.hamcrest.Matcher
import pl.patrykzygo.videospace.constants.MovieStatus
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.app.SimpleMovie
import pl.patrykzygo.videospace.data.local.MovieEntity
import pl.patrykzygo.videospace.data.network.EntryPointMoviesResponse
import pl.patrykzygo.videospace.data.network.MovieResponse
import retrofit2.Response

fun fakeCorrectMoviesResponseUi(page: Int, numberOfPages: Int): Response<EntryPointMoviesResponse> {
    val moviesResponse = EntryPointMoviesResponse(
        page = page,
        fakeMoviesResponseListUi(page),
        34234,
        numberOfPages
    )
    return Response.success(moviesResponse)
}

fun fakeHttpErrorResponseUi(): Response<EntryPointMoviesResponse> {
    return Response.error(
        404,
        ResponseBody.create(
            MediaType.parse("application/json"),
            "{\"key\":[\"somestuff\"]}"
        )
    )
}


private fun fakeMoviesResponseListUi(page: Int): List<MovieResponse> {
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

fun provideMovieWithIdUi(id: Int): Movie {
    return Movie(
        false, "", listOf(), id,
        "lang", "title $id", "descritpion $id",
        0.0, "poster $id", "release date",
        "title $id", false, 1.00, 323
    )
}

fun provideSimpleMovieWithIdUi(id: Int): SimpleMovie {
    return SimpleMovie(
        id, "Title $id", "Release date: $id", posterPath = "Poster of $id"
    )
}

fun createMovieEntity(id: Int, status: String = MovieStatus.UNASSIGNED): MovieEntity {
    return MovieEntity(id, title = "title $id", status = status, releaseDate = "")
}


fun clickChildWithId(id: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
        }

        override fun getDescription(): String {
            return "Description"
        }

        override fun perform(uiController: UiController?, view: View?) {
            val img = view?.findViewById<View>(id)
            img?.performClick()
        }
    }

}
