package com.devcode.githubappsfinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.devcode.githubappsfinal.ui.followerfollowing.FolllowerNFollowingOnFragment

class ViewPagerAdapterOnFragment(activity: FragmentActivity): FragmentStateAdapter(activity) {
    var username: String = ""
    override fun createFragment(position: Int): Fragment {
        val fragment = FolllowerNFollowingOnFragment()
        fragment.arguments = Bundle().apply {
            putInt(FolllowerNFollowingOnFragment.ARG_SECTION_NUMBER, position + 1)
            putString(FolllowerNFollowingOnFragment.ARG_NAME, username)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}