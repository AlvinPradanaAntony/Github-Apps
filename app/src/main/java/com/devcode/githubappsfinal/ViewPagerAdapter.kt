package com.devcode.githubappsfinal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.devcode.githubappsfinal.ui.followerfollowing.FolllowerNFollowing

class ViewPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    var username: String = ""
    override fun createFragment(position: Int): Fragment {
        val fragment = FolllowerNFollowing()
        fragment.arguments = Bundle().apply {
            putInt(FolllowerNFollowing.ARG_SECTION_NUMBER, position + 1)
            putString(FolllowerNFollowing.ARG_NAME, username)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}