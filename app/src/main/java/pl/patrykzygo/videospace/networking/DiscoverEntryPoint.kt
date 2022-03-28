package pl.patrykzygo.videospace.networking

import pl.patrykzygo.videospace.BuildConfig
import pl.patrykzygo.videospace.constants.SortOptions
import pl.patrykzygo.videospace.data.network.EntryPointMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoverEntryPoint {

    @GET("movie")
    suspend fun requestMoviesWithParameters(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("sort_by") sortOptions: String = SortOptions.POPULARITY_DESC,
        @Query("page") page: Int = 1,
        @Query("with_genres") includedGenres: String? = null,
        @Query("without_genres") excludedGenres: String? = null,
        @Query("vote_average.gte") minScore: Int? = null,
        @Query("vote_count.gte") minVoteCount: Int? = null
    ): Response<EntryPointMoviesResponse>
}