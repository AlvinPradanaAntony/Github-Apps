package com.devcode.githubappsfinal.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.devcode.githubappsfinal.DetailActivity
import com.devcode.githubappsfinal.adapter.UsersAdapter
import com.devcode.githubappsfinal.databinding.FragmentHomeBinding
import com.devcode.githubappsfinal.models.MainViewModel
import com.devcode.githubappsfinal.remote.UsersResponsesItem

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val list = ArrayList<UsersResponsesItem>()
    private val adapter: UsersAdapter by lazy {
        UsersAdapter(list)
    }
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recycleView.layoutManager = layoutManager

        setUpSearchView()
        ListDataUser()
        observeLoading()
    }

    private fun setUpSearchView() {
        with(binding) {
            search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    showLoading(true)
                    mainViewModel.getUserBySearch(query)
                    mainViewModel.searchUser.observe(requireActivity()) { searchUserResponse ->
                        if (searchUserResponse != null) {
                            adapter.addData(searchUserResponse)
                            setRecycleView()
                        }
                    }
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun ListDataUser(){
        mainViewModel.listuser.observe(viewLifecycleOwner) { userResponse ->
            if (userResponse != null) {
                adapter.addData(userResponse)
                setRecycleView()
            }
        }
        mainViewModel.searchUser.observe(viewLifecycleOwner) { searchUserResponse ->
            if (searchUserResponse != null) {
                adapter.addData(searchUserResponse)
                setRecycleView()
            }
        }
    }

    private fun observeLoading() {
        mainViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }
    }

    private fun setRecycleView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recycleView.layoutManager = layoutManager
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.adapter = adapter
        adapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UsersResponsesItem) {
                val intent = Intent(requireActivity(), DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STATE, data.login)
                intent.putExtra(DetailActivity.EXTRA_ID, data.id)
                intent.putExtra(DetailActivity.EXTRA_AVATAR_URL, data.avatarUrl)
                intent.putExtra(DetailActivity.EXTRA_URL, data.htmlUrl)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}