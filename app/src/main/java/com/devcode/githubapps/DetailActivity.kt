package com.devcode.githubapps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.devcode.githubapps.databinding.ActivityDetailBinding
import com.devcode.githubapps.models.DetailViewModel
import com.devcode.githubapps.remote.DetailUsersResponses
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(EXTRA_STATE)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR_URL)
        val url = intent.getStringExtra(EXTRA_URL)
        val bundle = Bundle()
        bundle.putString(EXTRA_STATE, name)
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = name

        if (name != null) {
            detailDataUsers(name, id, avatarUrl, url)
        }

    }

    private fun detailDataUsers(username: String, id: Int, avatarUrl: String?, url: String?) {
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.setUserDetail(username)
        viewModel.getUserDetail().observe(this) {
            if (it != null) {
                setData(it)
                var _isChecked = false
                CoroutineScope(Dispatchers.IO).launch {
                    val count = viewModel.checkUser(id)
                    withContext(Dispatchers.Main) {
                        if (count != null) {
                            if (count > 0) {
                                binding.favButton.setImageResource(R.drawable.ic_baseline_favorite_24)
                                _isChecked = true

                            } else {
                                binding.favButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                                _isChecked = false
                            }
                        }
                    }
                }
                binding.favButton.setOnClickListener {
                    _isChecked = !_isChecked
                    if (_isChecked) {
                        viewModel.addToFavorite(username, id, avatarUrl.toString(), url.toString())
                        binding.favButton.setImageResource(R.drawable.ic_baseline_favorite_24)
                        Snackbar.make(binding.root, "Added to Favorite", Snackbar.LENGTH_SHORT).show()
                    } else {
                        viewModel.deleteFromFavorite(id)
                        binding.favButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                        Snackbar.make(binding.root, "Removed from Favorite", Snackbar.LENGTH_SHORT).show()
                    }
                }
                setTabLayoutAdapter(it)
            }
        }
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
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"
        const val EXTRA_URL = "extra_url"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}