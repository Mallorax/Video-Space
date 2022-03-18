package pl.patrykzygo.videospace.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import pl.patrykzygo.videospace.data.local.GenreDao
import pl.patrykzygo.videospace.data.local.MoviesDao
import pl.patrykzygo.videospace.data.local.VideoSpaceDatabase
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RoomModule::class]
)
object TestRoomModule {

    @Provides
    @Singleton
    fun provideInMemoryDb(
        @ApplicationContext context: Context
    ): VideoSpaceDatabase {
        return Room.inMemoryDatabaseBuilder(context, VideoSpaceDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun provideGenresDao(db: VideoSpaceDatabase): GenreDao {
        return db.genreDao()
    }

    @Singleton
    @Provides
    fun provideMoviesDao(db: VideoSpaceDatabase): MoviesDao {
        return db.moviesDao()
    }

}