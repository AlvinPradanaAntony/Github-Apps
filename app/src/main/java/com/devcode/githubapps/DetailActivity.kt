package com.devcode.githubapps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.devcode.githubapps.databinding.ActivityDetailBinding
import com.devcode.githubapps.models.DetailViewModel
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
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(EXTRA_STATE)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = name
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        if (name != null) {
            detailDataUsers(name)
        }

    }

    private fun detailDataUsers(username: String) {
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.setUserDetail(username)
        viewModel.getUserDetail().observe(this) {
            if (it != null) {
                setData(it)
                setTabLayoutAdapter(it)
            }
        }
/*        mainViewModel.snackMsg.observe(this@DetailActivity) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                val snackbar = Snackbar.make(
                    findViewById(android.R.id.content),
                    errorMessage,
                    Snackbar.LENGTH_INDEFINITE
                )
                snackbar.setAction("Retry") {
                    mainViewModel.getDetailUser(username.toString())
                    snackbar.dismiss()
                }.show()
            }
        }*/
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
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        internal val TAG = DetailActivity::class.java.simpleName
        const val EXTRA_STATE = "extra_state"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}