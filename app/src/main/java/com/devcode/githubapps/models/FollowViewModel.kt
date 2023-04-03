package com.devcode.githubapps.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devcode.githubapps.remote.ApiConfig
import com.devcode.githubapps.remote.UsersResponsesItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    private val _isLoadingFollower = MutableLiveData<Boolean>()
    private val _isLoadingFollowing = MutableLiveData<Boolean>()

    val isLoadingFollower: LiveData<Boolean> = _isLoadingFollower
    val isLoadingFollowing: LiveData<Boolean> = _isLoadingFollowing

    init {
        _isLoadingFollower.value = true
        _isLoadingFollowing.value = true
    }


    private val _followerlivedata = MutableLiveData<ArrayList<UsersResponsesItem>>()
    val followerlivedata: LiveData<ArrayList<UsersResponsesItem>> = _followerlivedata

    private val _followinglivedata = MutableLiveData<ArrayList<UsersResponsesItem>>()
    val followinglivedata: LiveData<ArrayList<UsersResponsesItem>> = _followinglivedata

    fun follower(username: String) {
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<ArrayList<UsersResponsesItem>> {
            override fun onResponse(
                call: Call<ArrayList<UsersResponsesItem>>,
                response: Response<ArrayList<UsersResponsesItem>>
            ) {
                if (response.isSuccessful) {
                    _followerlivedata.value = response.body()
                    _isLoadingFollower.value = false
                }
            }

            override fun onFailure(call: Call<ArrayList<UsersResponsesItem>>, t: Throwable) {
            }
        })
    }

    fun following(username: String) {
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<ArrayList<UsersResponsesItem>> {
            override fun onResponse(
                call: Call<ArrayList<UsersResponsesItem>>,
                response: Response<ArrayList<UsersResponsesItem>>
            ) {
                if (response.isSuccessful) {
                    _followinglivedata.value = response.body()
                    _isLoadingFollowing.value = false
                }
            }

            override fun onFailure(call: Call<ArrayList<UsersResponsesItem>>, t: Throwable) {
            }
        })
    }
}