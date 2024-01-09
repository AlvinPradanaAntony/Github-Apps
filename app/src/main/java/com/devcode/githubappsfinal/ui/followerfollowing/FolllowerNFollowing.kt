package com.devcode.githubappsfinal.ui.followerfollowing

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.devcode.githubappsfinal.DetailActivity
import com.devcode.githubappsfinal.adapter.FollowAdapter
import com.devcode.githubappsfinal.databinding.FragmentFolllowerNFollowingBinding
import com.devcode.githubappsfinal.models.MainViewModel
import com.devcode.githubappsfinal.remote.UsersResponsesItem

class FolllowerNFollowing : Fragment() {
    private var _binding: FragmentFolllowerNFollowingBinding? = null
    private val binding get() = _binding!!
    private val list = ArrayList<UsersResponsesItem>()
    private val adapter: FollowAdapter by lazy {
        FollowAdapter(list)
    }
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFolllowerNFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val user = arguments?.getString(ARG_NAME).toString()

        if (position == 1) {
            val mIndex = 1
            ListDataUsers(user, mIndex)
            observeLoading()
        } else {
            val mIndex = 2
            ListDataUsers(user, mIndex)
            observeLoading()
        }
    }

    private fun ListDataUsers(username: String, index: Int) {
        if (index == 1) {
            mainViewModel.getListFollowers(username)
            mainViewModel.followers.observe(viewLifecycleOwner) { userResponse ->
                if (userResponse != null) {
                    adapter.addData(userResponse)
                    setRecycleView()
                }
            }
        } else {
            mainViewModel.getListFollowing(username)
            mainViewModel.following.observe(viewLifecycleOwner) { userResponse ->
                if (userResponse != null) {
                    adapter.addData(userResponse)
                    setRecycleView()
                }
            }
        }
    }

    private fun setRecycleView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.flowRecyclerView.layoutManager = layoutManager
        binding.flowRecyclerView.setHasFixedSize(true)
        binding.flowRecyclerView.adapter = adapter
        adapter.setOnItemClickCallback(object : FollowAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UsersResponsesItem) {
                val intent = Intent(requireActivity(), DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STATE, data.login)
                startActivity(intent)
            }
        })
    }

    private fun observeLoading() {
        mainViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.flowProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_NAME = "app_name"
        private const val TAG = "ViewPagerFollows"
    }
}