package com.example.messengerapp.presentation.fragment.chat

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.R
import com.example.messengerapp.presentation.elm.chat.model.ChatEvent
import com.example.messengerapp.presentation.recyclerview.base.paginetion.RecyclerViewPaginator
import com.example.messengerapp.util.Constants
import java.util.*

class TopicChatFragment : ChatFragment() {

    override val onScrollListener: RecyclerView.OnScrollListener
        get() = object : RecyclerViewPaginator(layoutManager!!, Constants.MESSAGE_THRESHOLD) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                store.accept(ChatEvent.Ui.LoadNextPage(streamId, topicName))
            }
        }

    override fun initUi() {
        super.initUi()
        binding.chatTopicName.isVisible = true
        binding.chatTopicName.text = resources.getString(
            R.string.topic_title,
            topicName.toLowerCase(Locale.ROOT)
        )
    }

    override fun sendMessage(messageText: String, messageId: Long) {
        if (messageId > 0)
            store.accept(ChatEvent.Ui.EditMessage(messageId, messageText))
        else
            store.accept(ChatEvent.Ui.SendMessage(messageText, streamId, topicName))
        binding.messageEditText.setText("")
    }
}