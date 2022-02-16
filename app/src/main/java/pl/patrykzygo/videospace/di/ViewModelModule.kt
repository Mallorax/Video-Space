package pl.patrykzygo.videospace.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import pl.patrykzygo.videospace.data.local.GenreDao
import pl.patrykzygo.videospace.data.local.MoviesDao
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import pl.patrykzygo.videospace.repository.LocalStoreRepository
import pl.patrykzygo.videospace.repository.LocalStoreRepositoryImpl
import pl.patrykzygo.videospace.repository.MoviesPagingSource
import pl.patrykzygo.videospace.repository.MoviesPagingSourceImpl

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideMoviesPagingSource(moviesEntryPoint: MoviesEntryPoint): MoviesPagingSource {
        return MoviesPagingSourceImpl(moviesEntryPoint)
    }

    @Provides
    @ViewModelScoped
    fun provideLocalStoreRepository(
        moviesDao: MoviesDao,
        genreDao: GenreDao,
        moviesEntryPoint: MoviesEntryPoint
    ): LocalStoreRepository {
        return LocalStoreRepositoryImpl(moviesDao, genreDao, moviesEntryPoint)
    }
}