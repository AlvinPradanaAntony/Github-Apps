package com.devcode.githubapps.adapter

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devcode.githubapps.R
import com.devcode.githubapps.databinding.ItemRowBinding
import com.devcode.githubapps.remote.UsersResponsesItem
import com.example.githubuser.data.FavoriteUser

class FavouriteAdapter(private val listUsers: ArrayList<FavoriteUser>) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(var binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = listUsers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text = listUsers[position].login
        holder.binding.tvUsername.text = listUsers[position].url
        Glide.with(holder.itemView.context)
            .load(listUsers[position].avatar_url)
            .override(102, 102)
            .error(R.drawable.placeholder)
            .into(holder.binding.profileImage)
        Glide.with(holder.itemView.context)
            .load(listUsers[position].avatar_url)
            .override(350, 350)
            .error(R.drawable.placeholder)
            .into(holder.binding.ivProfile)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUsers[holder.adapterPosition]) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(items: ArrayList<FavoriteUser>) {
        listUsers.clear()
        listUsers.addAll(items)
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteUser)
    }
}