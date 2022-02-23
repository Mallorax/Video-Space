package pl.patrykzygo.videospace.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import pl.patrykzygo.videospace.repository.LocalStoreRepository
import pl.patrykzygo.videospace.repository.MoviesPagingSource
import pl.patrykzygo.videospace.ui.MainViewModelFactory
import javax.inject.Named

@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {

    @Provides
    @Named("main_vm_factory")
    fun provideViewModelFactory(
        @LocalRepoImplQualifier localStoreRepository: LocalStoreRepository,
        @MoviesPagingSourceImplQualifier moviesPagingSource: MoviesPagingSource
    ): MainViewModelFactory {
        return MainViewModelFactory(localStoreRepository, moviesPagingSource)
    }

}