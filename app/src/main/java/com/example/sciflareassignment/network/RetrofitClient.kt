package com.example.sciflareassignment.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

    class RetrofitClient {

        companion object{

            private const val baseUrl = "https://crudcrud.com/"

            fun getRetrofitInstance(): Retrofit {

                return Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
        }
    }