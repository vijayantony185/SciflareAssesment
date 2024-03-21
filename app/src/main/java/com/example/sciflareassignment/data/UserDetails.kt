package com.example.sciflareassignment.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class UserDetails(@SerializedName("data") var data: ArrayList<User>? = null)

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("mobile")
    var mobile: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("gender")
    var gender: String? = null
)