package pl.patrykzygo.videospace.di

import android.content.Context
import androidx.navigation.testing.TestNavHostController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.patrykzygo.videospace.R
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestModule {

    @Singleton
    @Provides
    fun provideTestNavController(@ApplicationContext context: Context): TestNavHostController {
        val navController = TestNavHostController(context)
        navController.setGraph(R.navigation.nav_graph)
        return navController
    }
}