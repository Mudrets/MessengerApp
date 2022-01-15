package com.example.messengerapp.presentation.recyclerview.chat.pagination

import com.example.messengerapp.domain.entity.chat.SendingStatus
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped

interface ChatListManager {

    val items: List<ViewTyped>

    val isLoading: Boolean

    fun showLoading()

    fun removeLoading()

    fun clear()

    fun setPage(pageIndex: Int, newElements: List<ViewTyped>)

    fun sendMessage(messageText: String, topicName: String): Long

    fun deleteMessage(messageId: Long)

    fun addEmojiToMessage(messageId: Long, emojiName: String, status: SendingStatus)

    fun removeEmojiFromMessage(messageId: Long, emojiName: String, status: SendingStatus)

    fun setSendingStatus(messageId: Long, status: SendingStatus, realId: Long = messageId)

    fun changeMessageContent(messageId: Long, status: SendingStatus, content: String)

    fun changeMessageTopic(messageId: Long, status: SendingStatus, topicName: String)
}