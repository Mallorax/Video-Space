package pl.patrykzygo.videospace.data.app

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pl.patrykzygo.videospace.constants.MovieStatus

@Parcelize
data class SimpleMovie(
    val movieId: Int,
    val title: String,
    val releaseDate: String,
    var voteAverage: Double = 0.00,
    var voteCount: Int = 0,
    var userScore: Int = 0,
    var status: String = MovieStatus.UNASSIGNED,
    val posterPath: String,
    var userNotes: String = ""
) : Parcelable {
}