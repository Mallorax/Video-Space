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
import pl.patrykzygo.videospace.networking.GenresEntryPoint
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import pl.patrykzygo.videospace.others.Constants.GENRES_BASE_URL
import pl.patrykzygo.videospace.others.Constants.MOVIES_BASE_URL
import pl.patrykzygo.videospace.others.Constants.VIDEO_SPACE_DB_NAME
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMoviesEntryPoint(): MoviesEntryPoint {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MOVIES_BASE_URL)
            .build()
            .create(MoviesEntryPoint::class.java)
    }

    @Singleton
    @Provides
    fun provideGenresEntryPoint(): GenresEntryPoint {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(GENRES_BASE_URL)
            .build()
            .create(GenresEntryPoint::class.java)
    }

    @Singleton
    @Provides
    fun provideVideoSpaceDatabase(@ApplicationContext context: Context)
            : VideoSpaceDatabase {
        return Room.databaseBuilder(
            context,
            VideoSpaceDatabase::class.java,
            VIDEO_SPACE_DB_NAME
        ).build()
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