package com.devcode.githubapps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.devcode.githubapps.ui.followerfollowing.FolllowerNFollowing

class ViewPagerAdapterOnFragment(activity: FragmentActivity): FragmentStateAdapter(activity) {
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