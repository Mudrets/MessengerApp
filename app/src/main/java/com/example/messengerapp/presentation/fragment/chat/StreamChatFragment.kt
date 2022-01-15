package com.example.messengerapp.presentation.fragment.chat

import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.R
import com.example.messengerapp.presentation.elm.chat.model.ChatEvent
import com.example.messengerapp.presentation.recyclerview.chat.AllTopicsChatPaginator
import com.example.messengerapp.presentation.recyclerview.chat.MessageUi
import com.example.messengerapp.util.Constants

class StreamChatFragment : ChatFragment() {

    private val topicNameFromTopicField: String
        get() = binding.streamChatTopicName.text.toString().trim('#')

    private var isChangingTopicEditText = true

    override val onScrollListener: RecyclerView.OnScrollListener
        get() = object :
            AllTopicsChatPaginator(
                layoutManager!!,
                Constants.STREAM_CHAT_MESSAGE_THRESHOLD,
                adapter
            ) {
            override fun setTopicName(topicName: String) {
                showTopicName(topicName)
            }

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                store.accept(ChatEvent.Ui.LoadNextPage(streamId, topicName))
            }

        }

    private fun getFirstMessageTopicName(): String? {
        val message = adapter.items.find { viewTyped ->
            viewTyped is MessageUi
        }
        return (message as? MessageUi)?.topicName
    }

    private fun showTopicName(topicName: String = "") {
        val firstTopicName = if (topicName.isBlank())
            getFirstMessageTopicName()
        else
            topicName
        if (!firstTopicName.isNullOrBlank() && isChangingTopicEditText) {
            binding.topicNameContainer.isVisible = true
            binding.streamChatTopicName.setText(
                getString(
                    R.string.topic_name_for_stream_chat,
                    firstTopicName
                )
            )
        }
    }

    override fun initUi() {
        super.initUi()
        showTopicName()

        binding.streamChatTopicName.onFocusChangeListener =
            View.OnFocusChangeListener { _, _ ->
                isChangingTopicEditText = false
            }
    }

    override fun sendMessage(messageText: String, messageId: Long) {
        when {
            messageId > 0 -> {
                store.accept(ChatEvent.Ui.EditMessage(messageId, messageText))
                binding.messageEditText.setText("")
            }
            topicNameFromTopicField.isNotBlank() -> {
                store.accept(
                    ChatEvent.Ui.SendMessage(messageText, streamId, topicNameFromTopicField)
                )
                binding.messageEditText.setText("")
            }
            else -> Toast.makeText(
                context,
                getString(R.string.empty_topic_name_warning),
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.topicNameContainer.isVisible = true
        isChangingTopicEditText = true
    }

    override fun normalState() {
        super.normalState()
        showTopicName()
    }

    override fun topicDelimiterClick(topicName: String) {
        setFragmentResult(
            resultKey, bundleOf(
                "streamName" to streamName,
                "streamId" to streamId,
                "topicName" to topicName
            )
        )
    }

}