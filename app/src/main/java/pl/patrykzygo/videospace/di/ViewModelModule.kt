package pl.patrykzygo.videospace.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import pl.patrykzygo.videospace.data.local.GenreDao
import pl.patrykzygo.videospace.data.local.MoviesDao
import pl.patrykzygo.videospace.networking.DiscoverEntryPoint
import pl.patrykzygo.videospace.networking.GenresEntryPoint
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import pl.patrykzygo.videospace.repository.discover_paging.DiscoverPagingSource
import pl.patrykzygo.videospace.repository.discover_paging.DiscoverPagingSourceImpl
import pl.patrykzygo.videospace.repository.local_store.LocalStoreGenresRepository
import pl.patrykzygo.videospace.repository.local_store.LocalStoreGenresRepositoryImpl
import pl.patrykzygo.videospace.repository.local_store.LocalStoreMoviesRepository
import pl.patrykzygo.videospace.repository.local_store.LocalStoreMoviesRepositoryImpl
import pl.patrykzygo.videospace.repository.movies_paging.MoviesPagingSource
import pl.patrykzygo.videospace.repository.movies_paging.MoviesPagingSourceImpl
import pl.patrykzygo.videospace.ui.dispatchers.DispatchersProvider
import pl.patrykzygo.videospace.ui.dispatchers.StandardDispatchers

//Provides all dependencies needed for view models

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideDispatchersProvider(): DispatchersProvider {
        return StandardDispatchers()
    }

    @Provides
    fun provideLocalStoreMoviesRepository(
        moviesDao: MoviesDao,
        moviesEntryPoint: MoviesEntryPoint
    ): LocalStoreMoviesRepository {
        return LocalStoreMoviesRepositoryImpl(moviesDao, moviesEntryPoint)
    }

    @Provides
    fun provideLocalStoreGenresRepository(
        genreDao: GenreDao,
        genresEntryPoint: GenresEntryPoint
    ): LocalStoreGenresRepository {
        return LocalStoreGenresRepositoryImpl(genreDao, genresEntryPoint)
    }

    @Provides
    fun provideMoviesPagingSource(moviesEntryPoint: MoviesEntryPoint): MoviesPagingSource {
        return MoviesPagingSourceImpl(moviesEntryPoint)
    }

    @Provides
    fun provideGenrePagingSource(discoverEntryPoint: DiscoverEntryPoint): DiscoverPagingSource {
        return DiscoverPagingSourceImpl(discoverEntryPoint)
    }
}