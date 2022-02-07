package pl.patrykzygo.videospace.data.network

import com.google.gson.annotations.SerializedName

data class EntryPointMoviesResponse(

    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val movieList: List<MovieResponse>,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)
