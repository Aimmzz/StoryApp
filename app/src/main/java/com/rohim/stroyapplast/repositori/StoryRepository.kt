package com.rohim.stroyapplast.repositori

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.rohim.stroyapplast.data.response.ListStoryItem
import com.rohim.stroyapplast.data.service.ApiService

//class StoryRepository(private val storyDatabase:StoryDatabase, private val apiService: ApiService, private val token:String) {
//    @OptIn(ExperimentalPagingApi::class)
//    fun getStory(): LiveData<PagingData<ListStoryItem>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 5
//            ),
//            remoteMediator = StoryRemoteMediator(storyDatabase, apiService,token),
//            pagingSourceFactory = {
//                storyDatabase.storyDao().getAllStory()
//            }
//        ).liveData
//    }
//}