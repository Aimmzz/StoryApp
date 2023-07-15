package com.rohim.stroyapplast

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.rohim.stroyapplast.data.response.ListStoryItem
import com.rohim.stroyapplast.repositori.UserRepository
import com.rohim.stroyapplast.ui.adapter.AllStoryAdapter
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repositori: UserRepository

    private lateinit var mainViewModel: MainViewModel
    private val successResponse = DataDummy().successResponse()
    private val errorResponse = DataDummy().errorResponse()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mainViewModel = MainViewModel(repositori)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    // Test dengan 2 skenario
    //1. Ketika berhasil memuat data cerita.
    //    - Memastikan data tidak null.
    //    - Memastikan jumlah data sesuai dengan yang diharapkan.
    //    - Memastikan data pertama yang dikembalikan sesuai.
    //2. Ketika tidak ada data cerita.
    //    - Memastikan jumlah data yang dikembalikan nol.

    @Test
    fun `scenario 1 get story success`() = runTest {
        val expectedResponse = MutableLiveData<PagingData<ListStoryItem>>()
        expectedResponse.value = PagingDataSource.snapshot(successResponse.listStory)
        Mockito.`when`(repositori.getStory()).thenReturn(expectedResponse)

        val actualStory = mainViewModel.getStories().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = AllStoryAdapter.DIFF_CALLBACK,
            updateCallback = ListCallback(),
            mainDispatcher = mainDispatcherRule.testDispatcher,
            workerDispatcher = mainDispatcherRule.testDispatcher,
        )
        differ.submitData(actualStory)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(successResponse.listStory, differ.snapshot())
        Assert.assertEquals(successResponse.listStory.size, differ.snapshot().size)
        Assert.assertEquals(successResponse.listStory[0], differ.snapshot()[0])
    }

    @Test
    fun `scenario 2 get story empty response`() = runTest {
        val expectedResponse = MutableLiveData<PagingData<ListStoryItem>>()
        expectedResponse.value = PagingDataSource.snapshot(errorResponse.listStory)
        Mockito.`when`(repositori.getStory()).thenReturn(expectedResponse)

        val actualStory = mainViewModel.getStories().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = AllStoryAdapter.DIFF_CALLBACK,
            updateCallback = ListCallback(),
            mainDispatcher = mainDispatcherRule.testDispatcher,
            workerDispatcher = mainDispatcherRule.testDispatcher,
        )
        differ.submitData(actualStory)
        Assert.assertEquals(errorResponse.listStory.size, differ.snapshot().size)
    }
}