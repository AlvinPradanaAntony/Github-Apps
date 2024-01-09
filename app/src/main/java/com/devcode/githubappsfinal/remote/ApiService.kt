package com.devcode.githubappsfinal.remote

import com.devcode.githubappsfinal.BuildConfig
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    companion object {
        private const val API_TOKEN = BuildConfig.API_KEY
    }
    @GET("users")
    @Headers("Authorization: $API_TOKEN")
    fun getUsers(): Call<ArrayList<UsersResponsesItem>>

    @GET("search/users")
    @Headers("Authorization: $API_TOKEN")
    fun getListUsers(
        @Query("q") username: String
    ): Call<GithubResponses>

    @GET("users/{username}")
    @Headers("Authorization: $API_TOKEN")
    fun getDetailUser(
        @Path("username") username: String?
    ): Call<DetailUsersResponses>

    @GET("users/{username}/followers")
    @Headers("Authorization: $API_TOKEN")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<ArrayList<UsersResponsesItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: $API_TOKEN")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<ArrayList<UsersResponsesItem>>
}