package com.example.sciflareassignment.network

import com.example.sciflareassignment.data.UserDetails
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("api/e8714847d29e404fb97c5d3a75971103/vijay")
    fun getUsers() : Call<ArrayList<UserDetails>>
}