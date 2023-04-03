package com.devcode.githubapps.ui.followerfollowing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.devcode.githubapps.DetailActivity
import com.devcode.githubapps.adapter.DetailAdapter
import com.devcode.githubapps.adapter.UsersAdapter
import com.devcode.githubapps.databinding.FragmentFolllowerNFollowingBinding
import com.devcode.githubapps.models.FollowViewModel
import com.devcode.githubapps.models.MainViewModel
import com.devcode.githubapps.remote.UsersResponsesItem
import com.google.android.material.snackbar.Snackbar

class FolllowerNFollowing : Fragment() {
    private var _binding : FragmentFolllowerNFollowingBinding? = null
    private val binding get()=_binding!!
    private val list = ArrayList<UsersResponsesItem>()
    private val adapter: DetailAdapter by lazy {
        DetailAdapter(list)
    }
    private lateinit var viewModel: FollowViewModel

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

        if (position == 1) {
            val mIndex = 1
            ListDataUsers(user.toString(), mIndex)
        } else {
            val mIndex = 2
            ListDataUsers(user.toString(), mIndex)
        }
    }

    private fun ListDataUsers(username: String, index: Int) {
        if (index == 1) {
            viewModel = ViewModelProvider(this)[FollowViewModel::class.java]
            viewModel.follower(username)
            viewModel.followerlivedata.observe(viewLifecycleOwner){
                adapter.addData(it)
                setRecycleView(it)
            }
            viewModel.isLoadingFollower.observe(requireActivity()) {
                showLoading(it)
            }
        } else {
            viewModel = ViewModelProvider(this)[FollowViewModel::class.java]
            viewModel.following(username)
            viewModel.followinglivedata.observe(viewLifecycleOwner){
                adapter.addData(it)
                setRecycleView(it)
            }
            viewModel.isLoadingFollowing.observe(requireActivity()) {
                showLoading(it)
            }
        }
    }


    private fun setRecycleView(userResponse: ArrayList<UsersResponsesItem>) {
        if (userResponse.isNotEmpty()) {
            val layoutManager = LinearLayoutManager(requireActivity())
            binding.flowRecyclerView.layoutManager = layoutManager
            binding.flowRecyclerView.setHasFixedSize(true)
            binding.flowRecyclerView.adapter = adapter
            adapter.setOnItemClickCallback(object : DetailAdapter.OnItemClickCallback {
                override fun onItemClicked(data: UsersResponsesItem) {
                    val intent = Intent(requireActivity(), DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_STATE, data.login)
                    startActivity(intent)
                }
            })
        } else{
            Log.d(TAG, "setRecycleView: $userResponse")
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