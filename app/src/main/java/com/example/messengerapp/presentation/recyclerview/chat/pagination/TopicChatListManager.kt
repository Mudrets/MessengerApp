package com.example.messengerapp.presentation.recyclerview.chat.pagination

import com.example.messengerapp.domain.entity.chat.Message
import com.example.messengerapp.domain.entity.chat.SendingStatus
import com.example.messengerapp.presentation.mapper.MessagesListToViewTypedListMapper
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped
import com.example.messengerapp.presentation.recyclerview.chat.DateUi
import com.example.messengerapp.presentation.recyclerview.chat.MessageUi
import javax.inject.Inject

open class TopicChatListManager @Inject constructor(
    protected val chatPaginationHelper: ChatPaginationHelper,
    protected val messagesListToViewTypedListMapper: MessagesListToViewTypedListMapper
) : ChatListManager {
    override val items: List<ViewTyped>
        get() = chatPaginationHelper.items

    override val isLoading: Boolean
        get() = chatPaginationHelper.isLoading

    override fun showLoading() {
        chatPaginationHelper.showLoad()
    }

    override fun removeLoading() {
        chatPaginationHelper.removeLoad()
    }

    override fun clear() {
        chatPaginationHelper.clear()
    }

    override fun setPage(pageIndex: Int, newElements: List<ViewTyped>) {
        chatPaginationHelper[pageIndex] = newElements
    }

    override fun sendMessage(messageText: String, topicName: String): Long {
        val message = Message.createSendingMessage(messageText, topicName)
        val viewTypedList = messagesListToViewTypedListMapper(listOf(message))
        val newElements = mutableListOf(viewTypedList[0])
        val firstDate = items.find { elem -> elem is DateUi }
        if (firstDate != viewTypedList[1] as DateUi)
            newElements.add(viewTypedList[1])
        chatPaginationHelper.addNewItems(newElements)
        return message.id
    }

    override fun deleteMessage(messageId: Long) {
        val messageIndex = chatPaginationHelper.items.indexOfFirst { viewTyped ->
            viewTyped is MessageUi && viewTyped.messageId == messageId
        }
        if (messageIndex != -1)
            chatPaginationHelper.deleteItem(messageIndex)
    }

    override fun addEmojiToMessage(messageId: Long, emojiName: String, status: SendingStatus) {
        doActionWithMessage(messageId) { messageIndex ->
            val newMessage = (items[messageIndex] as MessageUi).addEmoji(emojiName, status)
            chatPaginationHelper.changeItem(messageIndex, newMessage)
        }
    }

    override fun removeEmojiFromMessage(messageId: Long, emojiName: String, status: SendingStatus) {
        doActionWithMessage(messageId) { messageIndex ->
            val newMessage = (items[messageIndex] as MessageUi).removeEmoji(emojiName, status)
            chatPaginationHelper.changeItem(messageIndex, newMessage)
        }
    }

    override fun setSendingStatus(messageId: Long, status: SendingStatus, realId: Long) {
        doActionWithMessage(messageId) { messageIndex ->
            val newMessage = (items[messageIndex] as MessageUi).copy(
                sendingStatus = status,
                messageId = realId
            )
            chatPaginationHelper.changeItem(messageIndex, newMessage)
        }
    }

    override fun changeMessageContent(messageId: Long, status: SendingStatus, content: String) {
        doActionWithMessage(messageId) { messageIndex ->
            val newMessage = (items[messageIndex] as MessageUi).copy(
                sendingStatus = status,
                messageText = content
            )
            chatPaginationHelper.changeItem(messageIndex, newMessage)
        }
    }

    override fun changeMessageTopic(messageId: Long, status: SendingStatus, topicName: String) {
        doActionWithMessage(messageId) { messageIndex ->
            val message = items[messageIndex] as MessageUi
            if (message.topicName != topicName)
                chatPaginationHelper.deleteItem(messageIndex)
        }
    }

    protected inline fun doActionWithMessage(
        messageId: Long,
        action: (Int) -> Unit
    ) {
        val messageIndex = items.indexOfFirst { viewTyped ->
            viewTyped is MessageUi && viewTyped.messageId == messageId
        }
        if (messageIndex != -1)
            action(messageIndex)
    }
}