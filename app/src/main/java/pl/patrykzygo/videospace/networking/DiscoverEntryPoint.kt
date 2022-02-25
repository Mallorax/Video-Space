package pl.patrykzygo.videospace.networking

import pl.patrykzygo.videospace.BuildConfig
import pl.patrykzygo.videospace.others.SortOptions
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoverEntryPoint {

    @GET("movie")
    suspend fun getMoviesWithGenre(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("sort_by") sortOptions: String = SortOptions.POPULARITY_DESC,
        @Query("page") page: Int = 1,
        @Query("with_genre") includedGenres: String
    )
}