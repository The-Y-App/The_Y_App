package com.rrc.adev3007.pixel_perfect.the_y_app.data

import com.rrc.adev3007.pixel_perfect.the_y_app.data.models.Post
import com.rrc.adev3007.pixel_perfect.the_y_app.data.models.CreatePostRequest
import com.rrc.adev3007.pixel_perfect.the_y_app.data.models.Media
import com.rrc.adev3007.pixel_perfect.the_y_app.data.models.UserAccount
import com.rrc.adev3007.pixel_perfect.the_y_app.data.models.UserAuth
import com.rrc.adev3007.pixel_perfect.the_y_app.data.models.UserAuthRequest
import com.rrc.adev3007.pixel_perfect.the_y_app.data.models.UserCreate
import com.rrc.adev3007.pixel_perfect.the_y_app.data.models.UserProfilePicture
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ISynchronizer {
    @GET("post")
    suspend fun getPosts(
        @Query("username") username: String,
        @Query("api_key") api_key: String,
        @Query("search") search: String? = null,
        @Query("dislikes_only") isDislikesOnly: Boolean? = null
    ) : Response<List<Post>>

    @PUT("post")
    suspend fun createPost(@Body data: CreatePostRequest) : Response<Any>

    @POST("login")
    suspend fun postLogin(@Body loginAuth: UserAuth ) : Response<UserAccount>

    @POST("logout")
    suspend fun postLogout(@Body data: UserAuthRequest): Response<Any>

    @PUT("user")
    suspend fun postUser(@Body createUser: UserCreate) : Response<Any>

    @PATCH("user")
    suspend fun patchUserProfilePicture(@Body createUser: UserProfilePicture) : Response<Any>

    @PUT("media")
    suspend fun postMedia(@Body createMedia: Media.MediaCreate) : Response<Media.MediaCreateResponse>

    @PUT("post/downvote/{post_id}")
    suspend fun addPostDislike(
        @Path("post_id") postId: Int,
        @Body body: Map<String,String>
    ) : Response<Map<String,String>>

    @HTTP(method = "DELETE", path = "post/downvote/{post_id}", hasBody = true)
    suspend fun deleteDownvote(
        @Path("post_id") postId: Int,
        @Body body: Map<String,String>
    ) : Response<Response<Map<String,String>>>
}

private const val BASE_URL = "https://the-y-app-api.azurewebsites.net/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

object Synchronizer {
    private val api: ISynchronizer by lazy {
        retrofit.create(ISynchronizer::class.java)
    }
    var unauthorizedCallback: (() -> Unit)? = null

    suspend fun <T> api(
        apiCall: suspend ISynchronizer.() -> Response<T>
    ): Response<T>? {
        var attempts = 0
        val maxRetryAttempts = 8
        var response: Response<T>? = null

        while (attempts < maxRetryAttempts) {
            try {
                response = api.apiCall()
                if (response.code() == 401) {
                    unauthorizedCallback?.invoke()
                }
                break
            } catch (e: Exception) {
                attempts++
                Thread.sleep(10000)
            }
        }
        return response
    }
}
