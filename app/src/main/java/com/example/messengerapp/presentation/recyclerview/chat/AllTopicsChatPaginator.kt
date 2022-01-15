package com.example.messengerapp.presentation.recyclerview.chat

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.presentation.recyclerview.base.AsyncAdapter
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped
import com.example.messengerapp.presentation.recyclerview.base.paginetion.RecyclerViewPaginator
import com.example.messengerapp.util.Constants

abstract class AllTopicsChatPaginator(
    private val layoutManager: RecyclerView.LayoutManager,
    private val visibleThreshold: Int = Constants.STREAM_CHAT_MESSAGE_THRESHOLD,
    private val adapter: AsyncAdapter<ViewTyped>,
    private val minPageSize: Int = Constants.MIN_PAGE_SIZE
) : RecyclerViewPaginator(layoutManager, visibleThreshold, minPageSize) {

    abstract fun setTopicName(topicName: String)

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(view, dx, dy)

        var position = 0
        if (layoutManager is GridLayoutManager) {
            position = layoutManager.findFirstCompletelyVisibleItemPosition()
        } else if (layoutManager is LinearLayoutManager) {
            position = layoutManager.findFirstCompletelyVisibleItemPosition()
        }

        if (adapter.items.isNotEmpty() && position >= 0) {
            when (val item = adapter.items[position]) {
                is MessageUi -> setTopicName(item.topicName)
                is TopicDelimiter -> setTopicName(item.topicName)
                else -> {
                }
            }
        }
    }

}