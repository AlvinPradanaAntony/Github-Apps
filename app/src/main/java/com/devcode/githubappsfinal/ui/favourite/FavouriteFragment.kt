package com.devcode.githubappsfinal.ui.favourite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.devcode.githubappsfinal.DetailActivity
import com.devcode.githubappsfinal.adapter.FavouriteAdapter
import com.devcode.githubappsfinal.databinding.FragmentFavouriteBinding
import com.devcode.githubappsfinal.models.FavoriteViewModel
import com.example.githubuser.data.FavoriteUser


class FavouriteFragment : Fragment() {
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() =  _binding!!
    private val list = ArrayList<FavoriteUser>()
    private val adapter: FavouriteAdapter by lazy {
        FavouriteAdapter(list)
    }
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recycleView.layoutManager = layoutManager
        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        adapter.notifyDataSetChanged()
        ListFavUser()
    }

    private fun ListFavUser(){
        showLoading(true)
        favoriteViewModel.getFavoriteUser()?.observe(viewLifecycleOwner) {
            if (it != null) {
                showLoading(false)
                adapter.addData(it as ArrayList<FavoriteUser>)
                setRecycleView()
            } else {
                Log.d("FavouriteFragment", "Error")
                showLoading(true)
            }
        }
    }

    private fun setRecycleView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recycleView.layoutManager = layoutManager
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.adapter = adapter
        adapter.setOnItemClickCallback(object : FavouriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FavoriteUser) {
                val intent = Intent(requireActivity(), DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STATE, data.login)
                intent.putExtra(DetailActivity.EXTRA_ID, data.id)
                intent.putExtra(DetailActivity.EXTRA_AVATAR_URL, data.avatar_url)
                intent.putExtra(DetailActivity.EXTRA_URL, data.url)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}