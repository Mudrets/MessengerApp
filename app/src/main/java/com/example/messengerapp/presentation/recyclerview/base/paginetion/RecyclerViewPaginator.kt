package com.example.messengerapp.presentation.recyclerview.base.paginetion

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.messengerapp.util.Constants


abstract class RecyclerViewPaginator(
    private val layoutManager: RecyclerView.LayoutManager,
    private val visibleThreshold: Int = Constants.MESSAGE_THRESHOLD,
    private val minPageSize: Int = Constants.MIN_PAGE_SIZE
) : RecyclerView.OnScrollListener() {

    private var currentPage = 0

    private var previousTotalItemCount = 0

    private var loading = true

    private val startingPageIndex = 0

    open fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(view, dx, dy)
        var lastVisibleItemPosition = 0
        val totalItemCount = layoutManager!!.itemCount
        when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions =
                    layoutManager.findLastVisibleItemPositions(null)
                // get maximum element within the list
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
            }
            is GridLayoutManager -> {
                lastVisibleItemPosition =
                    layoutManager.findLastVisibleItemPosition()
            }
            is LinearLayoutManager -> {
                lastVisibleItemPosition =
                    layoutManager.findLastVisibleItemPosition()
            }
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            currentPage = startingPageIndex
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount && totalItemCount >= minPageSize) {
            currentPage++
            onLoadMore(currentPage, totalItemCount, view)
            loading = true
        }
    }

    // Call this method whenever performing new searches
    fun resetState() {
        currentPage = startingPageIndex
        previousTotalItemCount = 0
        loading = true
    }

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?)
}