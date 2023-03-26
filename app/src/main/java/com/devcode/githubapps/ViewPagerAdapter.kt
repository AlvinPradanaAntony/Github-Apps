package com.devcode.githubapps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.devcode.githubapps.ui.followerfollowing.FolllowerNFollowing

class ViewPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        val fragment = FolllowerNFollowing()
        fragment.arguments = Bundle().apply {
            putInt(FolllowerNFollowing.ARG_SECTION_NUMBER, position + 1)
        }
        return fragment
    }
    override fun getItemCount(): Int {
        return 2
    }

}