package com.example.sciflareassignment.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class UsersDataBase : RoomDatabase() {
    abstract fun userDao() : UsersDao

    companion object {
        @Volatile
        private var INSTANCE: UsersDataBase? = null

        fun getInstance(context: Context): UsersDataBase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): UsersDataBase {
            return Room.databaseBuilder(
                context.applicationContext,
                UsersDataBase::class.java,
                "users-database"
            ).build()
        }
    }
}