package pl.patrykzygo.videospace.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.patrykzygo.videospace.constants.MovieStatus

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val movieId: Int,
    val title: String,
    val releaseDate: String,
    val score: Int = 0,
    val status: String = MovieStatus.UNASSIGNED,
    val dateSaved: Long = System.currentTimeMillis(),
    val posterPath: String = "",
    val userNotes: String = ""
) {
}