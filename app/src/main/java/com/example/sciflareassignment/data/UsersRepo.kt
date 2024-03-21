package com.example.sciflareassignment.data

import com.example.sciflareassignment.network.ApiInterface
import com.example.sciflareassignment.network.RetrofitClient

class UsersRepo {
    private val retrofit = RetrofitClient.getRetrofitInstance().create(ApiInterface::class.java)

    fun getUsers() = retrofit.getUsers()
}