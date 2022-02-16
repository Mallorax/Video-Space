package pl.patrykzygo.videospace.di

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.patrykzygo.videospace.FakeMoviesPagingSource
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.data.local.VideoSpaceDatabase
import pl.patrykzygo.videospace.repository.MoviesPagingSource
import javax.inject.Named

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

    @Provides
    fun provideTestNavController(@ApplicationContext context: Context): NavController {
        val navController = TestNavHostController(context)
        navController.setGraph(R.navigation.nav_graph)
        return navController
    }

    @Provides
    @Named("fake_movies_source")
    fun provideFakeMoviesSource(): MoviesPagingSource {
        return FakeMoviesPagingSource()
    }

}