package com.aethink.paging3helloworld.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.aethink.paging3helloworld.model.New
import com.aethink.paging3helloworld.repository.PagingSource
import kotlinx.coroutines.flow.Flow

class MainViewModel: ViewModel() {
    //
    // Public properties
    //
    val newsFlow: Flow<PagingData<New>>  // The flow of pages that will be provided to the UI
        get() {
            val pageSize = 10
            val prefetchDistance = 3

            // The pageSize is used to suggest a value for page sizes, but is not enforced. A
            // PAging source may completely ignore this value and still return a valid page
            val pagingConfig = PagingConfig(pageSize = pageSize, prefetchDistance = prefetchDistance)
            // The pagingSourceFactory provides the way to create PagingSource instances
            val pagingSourceFactory = { PagingSource() }

            val pager = Pager(config = pagingConfig, pagingSourceFactory = pagingSourceFactory)

            return pager.flow
        }
}