package com.devcode.githubapps.ui.followerfollowing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.devcode.githubapps.DetailList
import com.devcode.githubapps.R
import com.devcode.githubapps.adapter.UsersAdapter
import com.devcode.githubapps.databinding.FragmentFolllowerNFollowingBinding
import com.devcode.githubapps.models.MainViewModel
import com.devcode.githubapps.remote.UsersResponsesItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FolllowerNFollowing : Fragment() {
    private var _binding: FragmentFolllowerNFollowingBinding? = null
    private val binding get() = _binding!!
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

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val user = arguments?.getString(ARG_NAME)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.flowRecyclerView.layoutManager = layoutManager
        binding.flowRecyclerView.setHasFixedSize(true)

        if (index == 1) {
            user?.let {
                val mIndex = 1
                ListDataUsers(it, mIndex)
            }
        } else {
            user?.let {
                val mIndex = 2
                ListDataUsers(it, mIndex)
            }
        }
    }

    private fun ListDataUsers(username: String, index: Int) {
        if(index == 1) {
            mainViewModel.isLoading.observe(requireActivity()) {
                showLoading(it)
            }
            mainViewModel.getListFollowers(username)
            mainViewModel.followers.observe(requireActivity()) { userResponse ->
                if (userResponse != null) {
                    adapter.addData(userResponse)
                    setRecycleView()
                }
            }
        } else{
            mainViewModel.isLoading.observe(requireActivity()) {
                showLoading(it)
            }
            mainViewModel.getListFollowing(username)
            mainViewModel.following.observe(requireActivity()) { userResponse ->
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
        adapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UsersResponsesItem) {
                val intent = Intent(requireActivity(), DetailList::class.java)
                intent.putExtra(DetailList.EXTRA_STATE, data.login)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.flowProgress.visibility = View.VISIBLE
        } else {
            binding.flowProgress.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}