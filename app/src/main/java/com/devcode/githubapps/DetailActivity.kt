package com.devcode.githubapps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.devcode.githubapps.databinding.ActivityDetailBinding
import com.devcode.githubapps.models.MainViewModel
import com.devcode.githubapps.remote.ApiConfig
import com.devcode.githubapps.remote.DetailUsersResponses
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val mainViewModel by viewModels<MainViewModel>()

    companion object {
        internal val TAG = DetailList::class.java.simpleName
        const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(DetailList.EXTRA_STATE)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = name

        detailDataUsers(name)
    }

    private fun detailDataUsers(username: String?) {
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.getDetailUser(username.toString())
        mainViewModel.detailUsers.observe(this@DetailActivity, { userResponse ->
            if (userResponse != null) {
                setData(userResponse)
            }
        })
    }

    private fun setData(userResponse: DetailUsersResponses) {
        with(binding) {
            tvUserdetailName.text = userResponse.name
            tvUserdetailNick.text = "@${userResponse.login}"
            tvFollowerCount.text = userResponse.followers.toString()
            tvFollowingCount.text = userResponse.following.toString()
            if (userResponse.bio != null){
                tvUserdetailBio.visibility = View.VISIBLE
                tvUserdetailBio.text = userResponse.bio
            } else {
                tvUserdetailBio.visibility = View.GONE
            }
            if (userResponse.company != null){
                viewCompany.visibility = View.VISIBLE
                tvCompany.text = userResponse.company
            } else {
                viewCompany.visibility = View.GONE
            }
            if (userResponse.location != null){
                viewLocation.visibility = View.VISIBLE
                tvLocation.text = userResponse.location
            } else {
                viewLocation.visibility = View.GONE
            }
            if (userResponse.email != null){
                viewEmail.visibility = View.VISIBLE
                tvMail.text = userResponse.email.toString()
            } else {
                viewEmail.visibility = View.GONE
            }
            Glide.with(this@DetailActivity)
                .load("${userResponse.avatarUrl}")
                .error(R.drawable.placeholder)
                .into(ivPhoto)
        }
    }
   /* private fun detailDataUsers(username: String?) {
        showLoading(true)
        showComponent(true)
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUsersResponses> {
            override fun onResponse(
                call: Call<DetailUsersResponses>,
                response: Response<DetailUsersResponses>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        showLoading(false)
                        showComponent(false)

                    }
                } else {
                    Snackbar.make(binding.root, "Error: ${response.message()}", Snackbar.LENGTH_LONG).show()
                    Log.e(DetailList.TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetailUsersResponses>, t: Throwable) {
                showLoading(true)
                showComponent(true)
                Snackbar.make(binding.root, "Error: ${t.message}", Snackbar.LENGTH_LONG).show()
                Log.e(DetailList.TAG, "onFailure: ${t.message}")
            }
        })
    }*/

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
/*    private fun showComponent(isLoading: Boolean) {
        if (isLoading) {
            binding.detailActivity.visibility = View.GONE
        } else {
            binding.detailActivity.visibility = View.VISIBLE
        }
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}