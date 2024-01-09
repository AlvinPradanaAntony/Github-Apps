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

class DetailAdapter(private val listUsers: ArrayList<UsersResponsesItem>) : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {
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
        holder.binding.tvUsername.text = listUsers[position].htmlUrl
        Glide.with(holder.itemView.context)
            .load(listUsers[position].avatarUrl)
            .error(R.drawable.placeholder)
            .into(holder.binding.profileImage)
        Glide.with(holder.itemView.context)
            .load(listUsers[position].avatarUrl)
            .error(R.drawable.placeholder)
            .into(holder.binding.ivProfile)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUsers[holder.adapterPosition]) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(items: ArrayList<UsersResponsesItem>) {
        listUsers.clear()
        listUsers.addAll(items)
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UsersResponsesItem)
    }
}