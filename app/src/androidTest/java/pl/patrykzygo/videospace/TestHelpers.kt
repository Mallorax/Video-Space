package pl.patrykzygo.videospace

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import pl.patrykzygo.videospace.data.app.Movie

fun provideMovieWithIdUi(id: Int): Movie {
    return Movie(
        false, "", listOf(), id,
        "lang", "title $id", "overview",
        0.0, "poster $id", "release date",
        "title", false, 1.00, 323
    )
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