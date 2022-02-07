package pl.patrykzygo.videospace.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [GenreEntity::class, MovieEntity::class],
    version = 2
)
abstract class VideoSpaceDatabase: RoomDatabase() {

    abstract fun genreDao(): GenreDao
    abstract fun moviesDao(): MoviesDao
}