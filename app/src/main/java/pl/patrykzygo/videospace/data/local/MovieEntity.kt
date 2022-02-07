package pl.patrykzygo.videospace.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val movieId: Int,
    @ColumnInfo(name = "is_favourite")
    val isFavourite: Boolean,
    @ColumnInfo(name = "is_on_watch_later")
    val isOnWatchLater: Boolean
) {
}