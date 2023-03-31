package com.devcode.githubapps.ui.followerfollowing

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.devcode.githubapps.DetailActivity
import com.devcode.githubapps.adapter.UsersAdapter
import com.devcode.githubapps.databinding.FragmentFolllowerNFollowingBinding
import com.devcode.githubapps.models.MainViewModel
import com.devcode.githubapps.remote.UsersResponsesItem
import com.google.android.material.snackbar.Snackbar

class FolllowerNFollowing : Fragment() {
    private lateinit var _binding: FragmentFolllowerNFollowingBinding
    private val binding get() = _binding
    private val list = ArrayList<UsersResponsesItem>()
    private val adapter: UsersAdapter by lazy {
        UsersAdapter(list)
    }
    private val mainViewModel by viewModels<MainViewModel>()

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_NAME = "app_name"
        private const val TAG = "ViewPagerFollows"
    }

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
        val user = arguments?.getString(ARG_NAME)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.flowRecyclerView.layoutManager = layoutManager
        binding.flowRecyclerView.setHasFixedSize(true)

        if (position == 1) {
            val mIndex = 1
            observeLoading()
            ListDataUsers(user.toString(), mIndex)
        } else {
            val mIndex = 2
            observeLoading()
            ListDataUsers(user.toString(), mIndex)
        }
    }

    private fun ListDataUsers(username: String, index: Int) {
        if (index == 1) {
            mainViewModel.getListFollowers(username)
            mainViewModel.followers.observe(viewLifecycleOwner) { userResponse ->
                if (userResponse != null) {
                    adapter.addData(userResponse)
                    setRecycleView(userResponse)
                }
            }
        } else {
            mainViewModel.getListFollowing(username)
            mainViewModel.following.observe(viewLifecycleOwner) { userResponse ->
                if (userResponse != null) {
                    adapter.addData(userResponse)
                    setRecycleView(userResponse)
                }
            }
        }
    }

    private fun observeLoading() {
        mainViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }
    }

    private fun setRecycleView(userResponse: ArrayList<UsersResponsesItem>) {
        if (userResponse.isNotEmpty()) {
            val layoutManager = LinearLayoutManager(requireActivity())
            binding.flowRecyclerView.layoutManager = layoutManager
            binding.flowRecyclerView.setHasFixedSize(true)
            binding.flowRecyclerView.adapter = adapter
            adapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback {
                override fun onItemClicked(data: UsersResponsesItem) {
                    val intent = Intent(requireActivity(), DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_STATE, data.login)
                    startActivity(intent)
                }
            })
        } else {
            Snackbar.make(
                binding.flowRecyclerView,
                "User response is empty",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.flowProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}