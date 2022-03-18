package pl.patrykzygo.videospace.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.patrykzygo.videospace.data.local.GenreDao
import pl.patrykzygo.videospace.data.local.MoviesDao
import pl.patrykzygo.videospace.networking.DiscoverEntryPoint
import pl.patrykzygo.videospace.networking.GenresEntryPoint
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import pl.patrykzygo.videospace.repository.genre_paging.GenrePagingSource
import pl.patrykzygo.videospace.repository.genre_paging.GenrePagingSourceImpl
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepository
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepositoryImpl
import pl.patrykzygo.videospace.repository.movies_paging.MoviesPagingSource
import pl.patrykzygo.videospace.repository.movies_paging.MoviesPagingSourceImpl
import pl.patrykzygo.videospace.ui.dispatchers.DispatchersProvider
import pl.patrykzygo.videospace.ui.dispatchers.StandardDispatchers
import pl.patrykzygo.videospace.ui.factories.DefaultFragmentFactory
import pl.patrykzygo.videospace.ui.factories.MainViewModelFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideDispatchersProvider(): DispatchersProvider {
        return StandardDispatchers()
    }

    @Provides
    fun provideLocalStoreRepository(
        moviesDao: MoviesDao,
        genreDao: GenreDao,
        moviesEntryPoint: MoviesEntryPoint,
        genresEntryPoint: GenresEntryPoint
    ): LocalStoreRepository {
        return LocalStoreRepositoryImpl(moviesDao, genreDao, moviesEntryPoint, genresEntryPoint)
    }


    @Provides
    fun provideMoviesPagingSource(moviesEntryPoint: MoviesEntryPoint): MoviesPagingSource {
        return MoviesPagingSourceImpl(moviesEntryPoint)
    }

    @Provides
    fun provideGenrePagingSource(discoverEntryPoint: DiscoverEntryPoint): GenrePagingSource {
        return GenrePagingSourceImpl(discoverEntryPoint)
    }

    @Provides
    fun provideViewModelFactory(
        localStoreRepository: LocalStoreRepository,
        moviesPagingSource: MoviesPagingSource,
        genresPagingSource: GenrePagingSource,
        dispatcher: DispatchersProvider
    ): MainViewModelFactory {
        return MainViewModelFactory(
            localStoreRepository,
            moviesPagingSource,
            genresPagingSource,
            dispatcher
        )
    }

    @Provides
    fun provideDefaultFragmentFactory(@Named("main_vm_factory") vmFactory: MainViewModelFactory): DefaultFragmentFactory {
        return DefaultFragmentFactory(vmFactory)
    }
}