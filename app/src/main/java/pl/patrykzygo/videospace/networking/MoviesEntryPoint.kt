package pl.patrykzygo.videospace.networking

import pl.patrykzygo.videospace.BuildConfig
import pl.patrykzygo.videospace.data.network.EntryPointMoviesResponse
import pl.patrykzygo.videospace.data.network.movie_details.MovieDetailsResponse
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
    ): Response<EntryPointMoviesResponse>

    @GET("top_rated")
    suspend fun requestTopRatedMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String? = null
    ): Response<EntryPointMoviesResponse>

    @GET("now_playing")
    suspend fun requestNowPlayingMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String? = null
    ): Response<EntryPointMoviesResponse>

    @GET("upcoming")
    suspend fun requestUpcomingMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String? = null
    ): Response<EntryPointMoviesResponse>

    @GET("{id}/recommendations")
    suspend fun requestRecommendationsForMovie(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1

    ): Response<EntryPointMoviesResponse>

    @GET("{id}/similar")
    suspend fun requestSimilarMovies(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<EntryPointMoviesResponse>

    @GET("{id}")
    suspend fun requestMovie(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US"
    ): Response<MovieDetailsResponse>

}