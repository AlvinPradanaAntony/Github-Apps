package com.devcode.githubapps.remote

import com.google.gson.annotations.SerializedName

data class GithubResponses(
	@field:SerializedName("total_count")
	val totalCount: Int? = null,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean? = null,

	@field:SerializedName("items")
	val items: ArrayList<UsersResponsesItem>? = null
)

data class UsersResponsesItem(
	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("login")
	val login: String? = null,

	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null,

	@field:SerializedName("html_url")
	val htmlUrl: String? = null,
)

data class DetailUsersResponses(
	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("login")
	val login: String? = null,

	@field:SerializedName("email")
	val email: Any? = null,

	@field:SerializedName("bio")
	val bio: String? = null,

	@field:SerializedName("followers")
	val followers: Int? = null,

	@field:SerializedName("following")
	val following: Int? = null,

	@field:SerializedName("twitter_username")
	val twitterUsername: Any? = null,

	@field:SerializedName("company")
	val company: String? = null,

	@field:SerializedName("public_repos")
	val public_repos: Int? = null,

	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null,

	@field:SerializedName("location")
	val location: String? = null,
)