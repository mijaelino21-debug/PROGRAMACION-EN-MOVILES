package com.lino.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lino.murocampus.data.PostRepositoryRemote
import com.lino.murocampus.data.remote.RetrofitClient

class PostsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostsViewModel(PostRepositoryRemote(RetrofitClient.api)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
