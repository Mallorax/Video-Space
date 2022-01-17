package pl.patrykzygo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.patrykzygo.data.local.GenreDao
import pl.patrykzygo.data.local.GenreEntity

@Database(
    entities = [GenreEntity::class],
    version = 1
)
abstract class VideoSpaceDatabase: RoomDatabase() {

    abstract fun genreDao(): GenreDao
}