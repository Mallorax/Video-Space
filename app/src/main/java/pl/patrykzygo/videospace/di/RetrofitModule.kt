package pl.patrykzygo.videospace.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.patrykzygo.videospace.networking.DiscoverEntryPoint
import pl.patrykzygo.videospace.networking.GenresEntryPoint
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import pl.patrykzygo.videospace.others.Paths
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//Provides all retrofit related dependencies

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideMoviesEntryPoint(): MoviesEntryPoint {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Paths.MOVIES_BASE_URL)
            .build()
            .create(MoviesEntryPoint::class.java)
    }

    @Singleton
    @Provides
    fun provideGenresEntryPoint(): GenresEntryPoint {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Paths.GENRES_BASE_URL)
            .build()
            .create(GenresEntryPoint::class.java)
    }

    @Singleton
    @Provides
    fun provideDiscoverEntryPoint(): DiscoverEntryPoint {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Paths.DISCOVER_BASE_URL)
            .build()
            .create(DiscoverEntryPoint::class.java)
    }

}