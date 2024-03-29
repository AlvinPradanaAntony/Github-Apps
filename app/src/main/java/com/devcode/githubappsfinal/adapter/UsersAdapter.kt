package com.devcode.githubappsfinal.adapter

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devcode.githubappsfinal.R
import com.devcode.githubappsfinal.databinding.ItemRowBinding
import com.devcode.githubappsfinal.remote.UsersResponsesItem

class UsersAdapter(private val listUsers: ArrayList<UsersResponsesItem>) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    private var extraHeight = dpToPx(88)

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
            .override(102, 102)
            .error(R.drawable.placeholder)
            .into(holder.binding.profileImage)
        Glide.with(holder.itemView.context)
            .load(listUsers[position].avatarUrl)
            .override(350, 350)
            .error(R.drawable.placeholder)
            .into(holder.binding.ivProfile)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUsers[holder.adapterPosition]) }

        if (position == listUsers.size - 1) {
            val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.bottomMargin = extraHeight
            holder.itemView.layoutParams = layoutParams
        } else {
            val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.bottomMargin = 0
            holder.itemView.layoutParams = layoutParams
        }
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

    private fun dpToPx(dp: Int): Int {
        val displayMetrics = Resources.getSystem().displayMetrics
        return (dp * displayMetrics.density).toInt()
    }
}