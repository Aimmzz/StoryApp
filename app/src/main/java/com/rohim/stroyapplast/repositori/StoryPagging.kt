package com.rohim.stroyapplast.repositori

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rohim.stroyapplast.data.preference.UserPreference
import com.rohim.stroyapplast.data.response.ListStoryItem
import com.rohim.stroyapplast.data.service.ApiService
import kotlinx.coroutines.flow.first

class StoryPagging(
    private val apiService: ApiService,
    private val pref: UserPreference
) : PagingSource<Int, ListStoryItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val token = "Bearer ${pref.getUserData().first().token}"
            val responseData = apiService.getStories(token, page, params.loadSize).listStory

            LoadResult.Page(
                data = responseData,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}