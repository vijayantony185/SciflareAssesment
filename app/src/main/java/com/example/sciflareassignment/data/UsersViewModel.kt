package com.example.sciflareassignment.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class UsersViewModel(val repo : UsersRepo) : ViewModel() {
    var _userDetails = MutableLiveData<UserDetails>()
    val userDetails : LiveData<UserDetails> = _userDetails

    fun getUsers(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getUsers().enqueue(object : retrofit2.Callback<ArrayList<UserDetails>>{
                override fun onResponse(call: Call<ArrayList<UserDetails>>, response: Response<ArrayList<UserDetails>>) {
                    val responseBody = response.body() as ArrayList<UserDetails>
                    if (response.isSuccessful && responseBody.size > 0) {
                        _userDetails.value = responseBody[0]
                    }
                }

                override fun onFailure(call: Call<ArrayList<UserDetails>>, t: Throwable) {

                }
            })
        }
    }
}