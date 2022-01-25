package pl.patrykzygo.videospace.networking

import pl.patrykzygo.videospace.data.network.PopularMoviesResponse
import pl.patrykzygo.videospace.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesEntryPoint {

    //TODO: Hardcoded language to simplify development,
    // change it to system language later
    @GET("popular")
    suspend fun requestPopularMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String? = null
    ): Response<PopularMoviesResponse>

}