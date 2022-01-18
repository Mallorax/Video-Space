package pl.patrykzygo.videospace.data.network

import com.google.gson.annotations.SerializedName

data class PopularMoviesResponse(

    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val moviesList: List<MoviesResponse>,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)
