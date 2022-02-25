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
import pl.patrykzygo.videospace.networking.DiscoverEntryPoint
import pl.patrykzygo.videospace.networking.GenresEntryPoint
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import pl.patrykzygo.videospace.others.DbConstants.VIDEO_SPACE_DB_NAME
import pl.patrykzygo.videospace.others.Paths.DISCOVER_BASE_URL
import pl.patrykzygo.videospace.others.Paths.GENRES_BASE_URL
import pl.patrykzygo.videospace.others.Paths.MOVIES_BASE_URL
import pl.patrykzygo.videospace.repository.genre_paging.GenrePagingSource
import pl.patrykzygo.videospace.repository.genre_paging.GenrePagingSourceImpl
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepository
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepositoryImpl
import pl.patrykzygo.videospace.repository.movies_paging.MoviesPagingSource
import pl.patrykzygo.videospace.repository.movies_paging.MoviesPagingSourceImpl
import pl.patrykzygo.videospace.ui.DefaultFragmentFactory
import pl.patrykzygo.videospace.ui.MainViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
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
    fun provideDiscoverEntryPoint(): DiscoverEntryPoint {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(DISCOVER_BASE_URL)
            .build()
            .create(DiscoverEntryPoint::class.java)
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

    @LocalRepoImplQualifier
    @Provides
    fun provideLocalStoreRepository(
        moviesDao: MoviesDao,
        genreDao: GenreDao,
        moviesEntryPoint: MoviesEntryPoint
    ): LocalStoreRepository {
        return LocalStoreRepositoryImpl(moviesDao, genreDao, moviesEntryPoint)
    }


    @MoviesPagingSourceImplQualifier
    @Provides
    fun provideMoviesPagingSource(moviesEntryPoint: MoviesEntryPoint): MoviesPagingSource {
        return MoviesPagingSourceImpl(moviesEntryPoint)
    }

    @GenrePagingSourceImplQualifier
    @Provides
    fun provideGenrePagingSource(discoverEntryPoint: DiscoverEntryPoint): GenrePagingSource{
        return GenrePagingSourceImpl(discoverEntryPoint)
    }

    @Provides
    @Named("main_vm_factory")
    fun provideViewModelFactory(
        @LocalRepoImplQualifier localStoreRepository: LocalStoreRepository,
        @MoviesPagingSourceImplQualifier moviesPagingSource: MoviesPagingSource
    ): MainViewModelFactory {
        return MainViewModelFactory(localStoreRepository, moviesPagingSource)
    }

    @DefaultFragmentFactoryQualifier
    @Provides
    fun provideDefaultFragmentFactory(@Named("main_vm_factory") vmFactory: MainViewModelFactory): DefaultFragmentFactory{
        return DefaultFragmentFactory(vmFactory)
    }
}