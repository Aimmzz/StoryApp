package com.rohim.stroyapplast

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rohim.stroyapplast.data.model.UserModel
import com.rohim.stroyapplast.data.response.ListStoryItem
import com.rohim.stroyapplast.repositori.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository): ViewModel() {
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
    fun getUser(): LiveData<UserModel> {
        return repository.getUserData()
    }
    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return  repository.getStory().cachedIn(viewModelScope)
    }

}