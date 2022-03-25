package pl.patrykzygo.videospace.data.network.movie_details

import com.google.gson.annotations.SerializedName

data class GenresResponse(

    @field:SerializedName("genres")
    val genres: List<GenreItem?>? = null
)