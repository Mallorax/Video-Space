package pl.patrykzygo.videospace.di

import android.content.Context
import androidx.fragment.app.FragmentFactory
import androidx.navigation.testing.TestNavHostController
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.testing.TestInstallIn
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.TestFragmentFactory

@Module
@TestInstallIn(
    components = [ActivityRetainedComponent::class],
    replaces = [ActivityModule::class]
)
object TestActivityModule {

    @ActivityRetainedScoped
    @Provides
    fun provideTestFragmentFactory(navHostController: TestNavHostController): FragmentFactory {
        return TestFragmentFactory(navHostController)
    }

}