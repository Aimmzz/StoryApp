package com.rohim.stroyapplast.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rohim.stroyapplast.data.response.ListStoryItem
import com.rohim.stroyapplast.di.Injection

//class ViewModelLocal(private val storyRepository: StoryRepository): ViewModel() {
//    val story: LiveData<PagingData<ListStoryItem>> by lazy {  storyRepository.getStory().cachedIn(viewModelScope) }
//}
//class ViewModelFactory2(private val context: Context) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(ViewModelLocal::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return ViewModelLocal(Injection.provideRepo(context)) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}