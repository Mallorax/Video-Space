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
import kotlinx.coroutines.test.runBlockingTest
import pl.patrykzygo.videospace.FakeLocalStoreRepositoryAndroid
import pl.patrykzygo.videospace.FakeMoviesPagingSource
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.TestFragmentFactory
import pl.patrykzygo.videospace.data.local.VideoSpaceDatabase
import pl.patrykzygo.videospace.data.mapMovieToMovieEntity
import pl.patrykzygo.videospace.repository.LocalStoreRepository
import pl.patrykzygo.videospace.repository.MoviesPagingSource
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
        runBlockingTest {
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


    @Provides
    @Named("test_vm_factory")
    fun provideViewModelFactory(
        @FakeLocalRepoQualifier localStoreRepository: LocalStoreRepository,
        @FakeMoviePagingSourceQualifier moviesPagingSource: MoviesPagingSource
    ): MainViewModelFactory {
        return MainViewModelFactory(localStoreRepository, moviesPagingSource)
    }


    @Provides
    @Named("with_details_nav")
    fun providesTestFragmentFactory(
        testNavHostController: TestNavHostController,
        @Named("test_vm_factory") viewModelFactory: MainViewModelFactory
    ): TestFragmentFactory {
        testNavHostController.setCurrentDestination(R.id.movie_details)
        return TestFragmentFactory(testNavHostController, viewModelFactory)
    }


}