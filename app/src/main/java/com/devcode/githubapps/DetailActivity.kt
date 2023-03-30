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
import com.devcode.githubapps.databinding.ActivityDetailBinding
import com.devcode.githubapps.models.MainViewModel
import com.devcode.githubapps.remote.ApiConfig
import com.devcode.githubapps.remote.DetailUsersResponses
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
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
                setTabLayoutAdapter(userResponse)
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

    private fun setTabLayoutAdapter(user: DetailUsersResponses) {
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPagerAdapter.username = user.login.toString()
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = viewPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
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