package pl.patrykzygo.videospace.di

import android.content.Context
import androidx.navigation.testing.TestNavHostController
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.TestFragmentFactory
import pl.patrykzygo.videospace.data.local.VideoSpaceDatabase
import pl.patrykzygo.videospace.data.mapMovieToMovieEntity
import pl.patrykzygo.videospace.fakes.FakeGenrePagingSourceAndroid
import pl.patrykzygo.videospace.fakes.FakeLocalStoreRepositoryAndroid
import pl.patrykzygo.videospace.fakes.FakeMoviesPagingSource
import pl.patrykzygo.videospace.repository.genre_paging.GenrePagingSource
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepository
import pl.patrykzygo.videospace.repository.movies_paging.MoviesPagingSource
import pl.patrykzygo.videospace.ui.MainViewModelFactory
import pl.patrykzygo.videospace.util.provideMovieWithIdUi
import javax.inject.Named


@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(
        @ApplicationContext context: Context
    ): VideoSpaceDatabase {
        return Room.inMemoryDatabaseBuilder(context, VideoSpaceDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }


    @FakeLocalRepoQualifier
    @Provides
    fun provideFakeRepoWithMovies(): LocalStoreRepository {
        val repo = FakeLocalStoreRepositoryAndroid()
        runBlocking {
            repo.insertFavourites(
                mapMovieToMovieEntity(provideMovieWithIdUi(1)),
                mapMovieToMovieEntity(provideMovieWithIdUi(2)),
                mapMovieToMovieEntity(provideMovieWithIdUi(3))
            )
        }
        return repo
    }

    @Provides
    fun provideTestNavController(@ApplicationContext context: Context): TestNavHostController {
        val navController = TestNavHostController(context)
        navController.setGraph(R.navigation.nav_graph)
        return navController
    }

    @FakeMoviePagingSourceQualifier
    @Provides
    fun provideFakeMoviesSource(): MoviesPagingSource {
        return FakeMoviesPagingSource()
    }

    @FakeGenrePagingSourceQualifier
    @Provides
    fun provideFakeGenrePagingSource(): GenrePagingSource {
        return FakeGenrePagingSourceAndroid()
    }


    @TestVMFactoryQualifier
    @Provides
    fun provideViewModelFactory(
        @FakeLocalRepoQualifier localStoreRepository: LocalStoreRepository,
        @FakeMoviePagingSourceQualifier moviesPagingSource: MoviesPagingSource,
        @FakeGenrePagingSourceQualifier genrePagingSource: GenrePagingSource
    ): MainViewModelFactory {
        return MainViewModelFactory(localStoreRepository, moviesPagingSource, genrePagingSource)
    }


    @TestFragmentFactoryQualifier
    @Provides
    fun providesTestFragmentFactory(
        testNavHostController: TestNavHostController,
        @TestVMFactoryQualifier viewModelFactory: MainViewModelFactory
    ): TestFragmentFactory {
        return TestFragmentFactory(testNavHostController, viewModelFactory)
    }


}