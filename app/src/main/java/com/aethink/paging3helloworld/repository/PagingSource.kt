package com.aethink.paging3helloworld.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aethink.paging3helloworld.model.New

/**
 * The PagingSource is responsible of interacting with the primary source of data.
 */
class PagingSource: PagingSource<Int, New>() {
    private val dataSource = DataSource()

    override fun getRefreshKey(state: PagingState<Int, New>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, New> {
        val page = params.key ?: 0
        val news = dataSource.getNews(page) ?: listOf<New>()

        val prevKey = if (dataSource.hasPreviousPage(page)) page - 1 else null
        val nextKey = if (dataSource.hasNextPage(page)) page + 1 else null

        val loadResult = LoadResult.Page(
            data = news,
            prevKey = prevKey,
            nextKey = nextKey
        )

        return loadResult
    }
}