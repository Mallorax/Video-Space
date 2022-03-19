package pl.patrykzygo.videospace.di

import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.runBlocking
import pl.patrykzygo.videospace.AndroidTestDispatcher
import pl.patrykzygo.videospace.data.app.Genre
import pl.patrykzygo.videospace.data.mapMovieToMovieEntity
import pl.patrykzygo.videospace.fakes.FakeGenrePagingSourceAndroid
import pl.patrykzygo.videospace.fakes.FakeLocalStoreGenresRepositoryAndroid
import pl.patrykzygo.videospace.fakes.FakeLocalStoreMoviesRepositoryAndroid
import pl.patrykzygo.videospace.fakes.FakeMoviesPagingSourceAndroid
import pl.patrykzygo.videospace.repository.genre_paging.GenrePagingSource
import pl.patrykzygo.videospace.repository.local_store.LocalStoreGenresRepository
import pl.patrykzygo.videospace.repository.local_store.LocalStoreMoviesRepository
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
    fun provideFakeRepoWithMovies(): LocalStoreMoviesRepository {
        val repo = FakeLocalStoreMoviesRepositoryAndroid()
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
    fun provideFakeRepoWithGenres(): LocalStoreGenresRepository {
        val genres = listOf(
            Genre(1, "1"),
            Genre(2, "2"),
            Genre(3, "3"),
            Genre(4, "4"),
            Genre(5, "5"),
            Genre(6, "6"),
            Genre(7, "7"),
        )
        return FakeLocalStoreGenresRepositoryAndroid(genres)
    }


    @Provides
    fun provideFakeMoviesSource(): MoviesPagingSource {
        return FakeMoviesPagingSourceAndroid()
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