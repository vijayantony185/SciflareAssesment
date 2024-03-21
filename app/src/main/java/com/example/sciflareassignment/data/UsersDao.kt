package com.example.sciflareassignment.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsersDao {

    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users")
    suspend fun getUsers() : List<User>

    @Delete
    suspend fun deleteUsers(users: List<User>)
}