package pl.patrykzygo.videospace.data.network.movie_details

import com.google.gson.annotations.SerializedName

data class GenreItem(

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("name")
    val name: String? = null
)
