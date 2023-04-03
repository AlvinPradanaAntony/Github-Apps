package com.devcode.githubapps.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devcode.githubapps.remote.ApiConfig
import com.devcode.githubapps.remote.DetailUsersResponses
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    val user  = MutableLiveData<DetailUsersResponses>()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init{
        _isLoading.value = true
    }

    //Set data dengan parameter username
    fun setUserDetail(username:String){
        ApiConfig.getApiService()
            .getDetailUser(username)
            .enqueue(object: Callback<DetailUsersResponses> {
                override fun onResponse(
                    call: Call<DetailUsersResponses>,
                    response: Response<DetailUsersResponses>
                ) {
                    if(response.isSuccessful) {
                        user.postValue(response.body())
                        _isLoading.value = false
                    }
                }
                override fun onFailure(call: Call<DetailUsersResponses>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    //Read data DetailResponse
    fun getUserDetail(): LiveData<DetailUsersResponses> {
        return user
    }
}