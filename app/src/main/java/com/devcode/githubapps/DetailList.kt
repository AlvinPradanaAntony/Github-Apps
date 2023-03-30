package com.devcode.githubapps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.devcode.githubapps.databinding.ActivityDetailListBinding
import com.devcode.githubapps.models.MainViewModel
import com.devcode.githubapps.remote.ApiConfig
import com.devcode.githubapps.remote.DetailUsersResponses
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailList : AppCompatActivity() {
    private lateinit var binding: ActivityDetailListBinding
    private val mainViewModel by viewModels<MainViewModel>()

    companion object {
        internal val TAG = DetailList::class.java.simpleName
        const val EXTRA_STATE = "extra_state"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_list)

        val name = intent.getStringExtra(EXTRA_STATE)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = name

        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPagerAdapter.username = name.toString()
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = viewPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
        detailDataUsers(name)
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
                        binding.tvUserdetailName.text = responseBody.name
                        binding.tvUserdetailNick.text = responseBody.login
                        binding.tvUserdetailBio.text = responseBody.bio
                        binding.tvCompany.text = responseBody.company
                        binding.tvFollowerCount.text = responseBody.followers.toString()
                        binding.tvFollowingCount.text = responseBody.following.toString()
                        binding.tvLocation.text = responseBody.location
                        Glide.with(this@DetailList)
                            .load("${responseBody.avatarUrl}")
                            .into(binding.ivPhoto)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetailUsersResponses>, t: Throwable) {
                showLoading(true)
                Log.e(TAG, "onFailure: ${t.message}")
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
}