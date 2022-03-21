package pl.patrykzygo.videospace.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.patrykzygo.videospace.others.MovieStatus

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val movieId: Int,
    val title: String,
    val score: Int = 0,
    val status: String = MovieStatus.UNASSIGNED,
    val date: Long = System.currentTimeMillis()
) {
}