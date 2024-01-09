package com.devcode.githubappsfinal.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.devcode.githubappsfinal.R
import com.devcode.githubappsfinal.ViewPagerAdapterOnFragment
import com.devcode.githubappsfinal.databinding.FragmentProfileBinding
import com.devcode.githubappsfinal.models.MainViewModel
import com.devcode.githubappsfinal.remote.DetailUsersResponses
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class ProfileFragment : Fragment() {
   private var _binding: FragmentProfileBinding? = null
   private val binding get() = _binding!!
   private val mainViewModel by viewModels<MainViewModel>()

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {
      _binding = FragmentProfileBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      val username = "alvinpradanaantony"
      detailDataUsers(username)
   }

   private fun detailDataUsers(username: String?) {
      mainViewModel.isLoading.observe(viewLifecycleOwner) {
         showLoading(it)
      }
      mainViewModel.getDetailUser(username.toString())
      mainViewModel.detailUsers.observe(viewLifecycleOwner) { userResponse ->
         if (userResponse != null) {
            setData(userResponse)
            setTabLayoutAdapter(userResponse)
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
         Glide.with(requireActivity())
            .load("${userResponse.avatarUrl}")
            .error(R.drawable.placeholder)
            .into(ivPhoto)
      }
   }

   private fun setTabLayoutAdapter(user: DetailUsersResponses) {
      val viewPagerAdapter = ViewPagerAdapterOnFragment(requireActivity())
      viewPagerAdapter.username = user.login.toString()
      val viewPager: ViewPager2 = binding.viewPager
      viewPager.adapter = viewPagerAdapter
      val tabs: TabLayout = binding.tabs
      TabLayoutMediator(tabs, viewPager) { tab, position ->
         tab.text = resources.getString(TAB_TITLES[position])
      }.attach()
   }

   private fun showLoading(isLoading: Boolean) {
      binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
   }

   companion object {
      internal val TAG = ProfileFragment::class.java.simpleName

      @StringRes
      private val TAB_TITLES = intArrayOf(
         R.string.tab_text_1,
         R.string.tab_text_2
      )
   }
}