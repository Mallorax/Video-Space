package pl.patrykzygo.videospace.di

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.testing.TestNavHostController
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.mockito.Mockito.spy
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.data.local.VideoSpaceDatabase
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
    fun testNavController(@ApplicationContext context: Context): NavController{
        val navController = TestNavHostController(context)
        navController.setGraph(R.navigation.nav_graph)
        return navController
    }
}