package pl.patrykzygo.videospace.networking

import pl.patrykzygo.videospace.BuildConfig
import pl.patrykzygo.videospace.data.network.auth.CreateSessionBody
import pl.patrykzygo.videospace.data.network.auth.CreateSessionIdResponse
import pl.patrykzygo.videospace.data.network.auth.RequestTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationEntryPoint {

    @GET("token/new")
    suspend fun requestAuthToken(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Response<RequestTokenResponse>

    @POST("session/new")
    suspend fun createSessionId(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Body body: CreateSessionBody
    ): Response<CreateSessionIdResponse>
}