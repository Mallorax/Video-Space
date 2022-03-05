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
    @ColumnInfo(name = "is_favourite")
    val isFavourite: Boolean, //TODO: Redundant field
    @ColumnInfo(name = "is_on_watch_later")
    val isOnWatchLater: Boolean, //TODO: Redundant field
    val score: Int = 0,
    val status: String = MovieStatus.UNASSIGNED,
    val date: Long = System.currentTimeMillis()
) {
}