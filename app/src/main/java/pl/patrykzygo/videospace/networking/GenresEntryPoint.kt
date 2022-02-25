package pl.patrykzygo.videospace.networking

import pl.patrykzygo.videospace.BuildConfig
import pl.patrykzygo.videospace.data.network.movie_details.GenresResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GenresEntryPoint {

    @GET("movie/list")
    suspend fun getGenresForMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String? = null
    ): Response<GenresResponse>

    @GET("tv/list")
    suspend fun getGenresForTvShows(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String? = null
    ): Response<GenresResponse>
}