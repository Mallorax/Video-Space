package pl.patrykzygo.videospace.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [GenreEntity::class],
    version = 1
)
abstract class VideoSpaceDatabase: RoomDatabase() {

    abstract fun genreDao(): GenreDao
}