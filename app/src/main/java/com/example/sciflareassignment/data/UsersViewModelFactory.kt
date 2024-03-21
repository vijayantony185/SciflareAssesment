package com.example.sciflareassignment.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class UsersViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UsersViewModel(repo = UsersRepo()) as T
    }
}