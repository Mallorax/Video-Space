package pl.patrykzygo.videospace.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.patrykzygo.videospace.data.local.GenreDao
import pl.patrykzygo.videospace.data.local.MoviesDao
import pl.patrykzygo.videospace.data.local.VideoSpaceDatabase
import pl.patrykzygo.videospace.others.DbConstants
import javax.inject.Singleton

//Provides all room related dependencies

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideVideoSpaceDatabase(@ApplicationContext context: Context)
            : VideoSpaceDatabase {
        return Room.databaseBuilder(
            context,
            VideoSpaceDatabase::class.java,
            DbConstants.VIDEO_SPACE_DB_NAME
        )
            .fallbackToDestructiveMigration()
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