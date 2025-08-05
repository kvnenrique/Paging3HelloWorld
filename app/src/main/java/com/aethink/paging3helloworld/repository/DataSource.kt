package com.aethink.paging3helloworld.repository

import com.aethink.paging3helloworld.model.New
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

/**
 * For this example data is just simulated. But data could come from network, local database, file,
 * etc.
 */
class DataSource {
    private companion object {
        val PAGES = 10
        val PAGE_SIZE = 10
    }

    //
    // Public interface
    //
    /**
     * returns the block of news for the given [page], of course it makes not sense to have
     * a function that fetches all news at once.
     */
    suspend fun getNews(page: Int): List<New>? {
        // We'll assume there are only 10 pages
        if (page in 0 ..< PAGES) {

            // simulated delay
            delay(0.5.seconds)

            val news = List<New>(PAGE_SIZE) {
                New("New ${(page * PAGE_SIZE) + it}")
            }
            return news
        } else {
            return null
        }
    }

    fun hasPreviousPage(page: Int) = if (page == 0) false else true
    fun hasNextPage(page: Int) = if (page >= PAGES-1) false else true
}