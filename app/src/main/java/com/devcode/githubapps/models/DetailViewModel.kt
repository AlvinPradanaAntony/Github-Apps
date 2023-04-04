package com.devcode.githubapps.models

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devcode.githubapps.remote.ApiConfig
import com.devcode.githubapps.remote.DetailUsersResponses
import com.example.githubuser.data.FavoriteUser
import com.example.githubuser.data.FavoriteUserDao
import com.example.githubuser.data.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    val user  = MutableLiveData<DetailUsersResponses>()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var userDao: FavoriteUserDao? = null
    private var userDb: UserDatabase? = null

    init{
        _isLoading.value = true
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

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
                    Toast.makeText(getApplication(), "Failed to load data", Toast.LENGTH_SHORT).show()
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    //Read data DetailResponse
    fun getUserDetail(): LiveData<DetailUsersResponses> {
        return user
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String, url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavoriteUser(username, id, avatarUrl, url)
            userDao?.addtoFavorite(user)
        }

    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun deleteFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.deleteFromFavorite(id)
        }
    }
}