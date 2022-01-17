package pl.patrykzygo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.patrykzygo.networking.MoviesEntryPoint
import pl.patrykzygo.others.Constants.MOVIES_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideMoviesEntryPoint(): MoviesEntryPoint {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MOVIES_BASE_URL)
            .build()
            .create(MoviesEntryPoint::class.java)
    }
}