package pl.patrykzygo.videospace.data.network.auth

import com.google.gson.annotations.SerializedName

data class CreateSessionIdResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("session_id")
    val sessionId: String
)
