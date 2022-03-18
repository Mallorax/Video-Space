package pl.patrykzygo.videospace.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import pl.patrykzygo.videospace.ui.factories.DefaultFragmentFactory

@Module
@InstallIn(ActivityRetainedComponent::class)
object ActivityModule {

    @Provides
    @ActivityRetainedScoped
    fun provideDefaultFragmentFactory(): DefaultFragmentFactory {
        return DefaultFragmentFactory()
    }
}