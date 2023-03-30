package com.devcode.githubapps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.devcode.githubapps.databinding.ActivityDetailBinding
import com.devcode.githubapps.remote.ApiConfig
import com.devcode.githubapps.remote.DetailUsersResponses
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

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
        showLoading(true)
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUsersResponses> {
            override fun onResponse(
                call: Call<DetailUsersResponses>,
                response: Response<DetailUsersResponses>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        with(binding) {
                            tvUserdetailName.text = responseBody.name
                            tvUserdetailNick.text = "@${responseBody.login}"
                            tvFollowerCount.text = responseBody.followers.toString()
                            tvFollowingCount.text = responseBody.following.toString()
                            if (responseBody.bio != null){
                                tvUserdetailBio.visibility = View.VISIBLE
                                tvUserdetailBio.text = responseBody.bio
                            } else {
                                tvUserdetailBio.visibility = View.GONE
                            }
                            if (responseBody.company != null){
                                viewCompany.visibility = View.VISIBLE
                                tvCompany.text = responseBody.company
                            } else {
                                viewCompany.visibility = View.GONE
                            }
                            if (responseBody.location != null){
                                viewLocation.visibility = View.VISIBLE
                                tvLocation.text = responseBody.location
                            } else {
                                viewLocation.visibility = View.GONE
                            }
                            if (responseBody.email != null){
                                viewEmail.visibility = View.VISIBLE
                                tvMail.text = responseBody.email.toString()
                            } else {
                                viewEmail.visibility = View.GONE
                            }
                            Glide.with(this@DetailActivity)
                                .load("${responseBody.avatarUrl}")
                                .error(R.drawable.placeholder)
                                .into(ivPhoto)
                        }
                    }
                } else {
                    Snackbar.make(binding.root, "Error: ${response.message()}", Snackbar.LENGTH_LONG).show()
                    Log.e(DetailList.TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetailUsersResponses>, t: Throwable) {
                showLoading(true)
                Snackbar.make(binding.root, "Error: ${t.message}", Snackbar.LENGTH_LONG).show()
                Log.e(DetailList.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

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