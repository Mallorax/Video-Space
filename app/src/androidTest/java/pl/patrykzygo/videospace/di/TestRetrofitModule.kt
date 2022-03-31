package pl.patrykzygo.videospace.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import pl.patrykzygo.videospace.networking.DiscoverEntryPoint
import pl.patrykzygo.videospace.networking.GenresEntryPoint
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RetrofitModule::class]
)
object TestRetrofitModule {

    @Singleton
    @Provides
    fun provideMoviesEntryPoint(): MoviesEntryPoint {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("")
            .build()
            .create(MoviesEntryPoint::class.java)
    }

    @Singleton
    @Provides
    fun provideGenresEntryPoint(): GenresEntryPoint {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("")
            .build()
            .create(GenresEntryPoint::class.java)
    }

    @Singleton
    @Provides
    fun provideDiscoverEntryPoint(): DiscoverEntryPoint {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("")
            .build()
            .create(DiscoverEntryPoint::class.java)
    }
}