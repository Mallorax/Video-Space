package pl.patrykzygo.videospace.di

import android.content.Context
import androidx.navigation.testing.TestNavHostController
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import pl.patrykzygo.videospace.AndroidTestDispatcher
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.ui.dispatchers.DispatchersProvider

@ExperimentalCoroutinesApi
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object AndroidTestAppModule {

    @Provides
    fun provideDispatchersProvider(): DispatchersProvider {
        return AndroidTestDispatcher()
    }

    @Provides
    fun provideTestNavController(@ApplicationContext context: Context): TestNavHostController {
        val navController = TestNavHostController(context)
        navController.setGraph(R.navigation.nav_graph)
        return navController
    }
}