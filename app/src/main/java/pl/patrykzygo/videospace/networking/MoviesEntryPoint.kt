package pl.patrykzygo.videospace.networking

import pl.patrykzygo.videospace.data.network.PopularMoviesResponse
import pl.patrykzygo.videospace.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("top_rated")
    suspend fun requestTopRatedMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String? = null
    ): Response<PopularMoviesResponse>

    @GET("now_playing")
    suspend fun requestNowPlayingMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String? = null
    ): Response<PopularMoviesResponse>

    @GET("upcoming")
    suspend fun requestUpcomingMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String? = null
    ): Response<PopularMoviesResponse>

    @GET("{id}/recommendations")
    suspend fun requestRecommendationsForMovie(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Path("id") id: Int
    ): Response<PopularMoviesResponse>

    @GET("{id}/similar")
    suspend fun requestSimilarMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Path("id") id: Int
    ): Response<PopularMoviesResponse>

}