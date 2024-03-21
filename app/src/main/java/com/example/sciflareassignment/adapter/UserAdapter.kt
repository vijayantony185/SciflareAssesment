package com.example.sciflareassignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sciflareassignment.R
import com.example.sciflareassignment.data.User
import com.example.sciflareassignment.databinding.AdapterUserBinding

class UserAdapter(val context: Context,val userList : ArrayList<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: AdapterUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User){
            binding.tvUserName.text = user.name
            binding.tvUserEmail.text = user.email
            binding.tvUserGender.text = user.gender
            binding.tvUserMobile.text = user.mobile
            if (user.gender.equals("female")) {
                Glide.with(context).load(R.drawable.woman).into(binding.imgUser)
            } else {
                Glide.with(context).load(R.drawable.man).into(binding.imgUser)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
    }
}