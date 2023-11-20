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
import retrofit2.http.DELETE
import retrofit2.http.FieldMap
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
        @Query("api_key") api_key: String
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

private const val BASE_URL = "http://192.168.100.101:5000/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

object Synchronizer {
    val api: ISynchronizer by lazy {
        retrofit.create(ISynchronizer::class.java)
    }
}
