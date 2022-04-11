package pl.patrykzygo.videospace.data.network.auth

import com.google.gson.annotations.SerializedName

data class CreateSessionBody(
    @SerializedName("request_token")
    val requestToken: String
)
