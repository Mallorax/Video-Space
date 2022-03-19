package pl.patrykzygo.videospace.di

import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.runBlocking
import pl.patrykzygo.videospace.AndroidTestDispatcher
import pl.patrykzygo.videospace.data.mapMovieToMovieEntity
import pl.patrykzygo.videospace.fakes.FakeGenrePagingSourceAndroid
import pl.patrykzygo.videospace.fakes.FakeLocalStoreRepositoryAndroid
import pl.patrykzygo.videospace.fakes.FakeMoviesPagingSource
import pl.patrykzygo.videospace.repository.genre_paging.GenrePagingSource
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepository
import pl.patrykzygo.videospace.repository.movies_paging.MoviesPagingSource
import pl.patrykzygo.videospace.ui.dispatchers.DispatchersProvider
import pl.patrykzygo.videospace.util.provideMovieWithIdUi

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [ViewModelModule::class]
)
object TestViewModelModule {

    @Provides
    fun provideFakeRepoWithMovies(): LocalStoreRepository {
        val repo = FakeLocalStoreRepositoryAndroid()
        runBlocking {
            repo.saveMoviesToDb(
                mapMovieToMovieEntity(provideMovieWithIdUi(1)),
                mapMovieToMovieEntity(provideMovieWithIdUi(2)),
                mapMovieToMovieEntity(provideMovieWithIdUi(3))
            )
        }
        return repo
    }


    @Provides
    fun provideFakeMoviesSource(): MoviesPagingSource {
        return FakeMoviesPagingSource()
    }

    @Provides
    fun provideFakeGenrePagingSource(): GenrePagingSource {
        return FakeGenrePagingSourceAndroid()
    }

    @Provides
    fun provideDispatchersProvider(): DispatchersProvider {
        return AndroidTestDispatcher()
    }
}