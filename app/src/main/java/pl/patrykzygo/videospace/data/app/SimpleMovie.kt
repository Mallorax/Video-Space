package pl.patrykzygo.videospace.data.app

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SimpleMovie(
    val movieId: Int,
    val title: String,
    val releaseDate: String,
    var score: Int = 0,
    var status: String,
    val posterPath: String,
    var userNotes: String = ""
): Parcelable {
}