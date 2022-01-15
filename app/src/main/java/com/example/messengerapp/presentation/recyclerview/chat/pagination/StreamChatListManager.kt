package com.example.messengerapp.presentation.recyclerview.chat.pagination

import com.example.messengerapp.domain.entity.chat.Message
import com.example.messengerapp.domain.entity.chat.SendingStatus
import com.example.messengerapp.presentation.mapper.MessagesListToViewTypedListMapper
import com.example.messengerapp.presentation.recyclerview.chat.DateUi
import com.example.messengerapp.presentation.recyclerview.chat.MessageUi
import com.example.messengerapp.presentation.recyclerview.chat.TopicDelimiter
import javax.inject.Inject

class StreamChatListManager @Inject constructor(
    chatPaginationHelper: ChatPaginationHelper,
    messagesListToViewTypedListMapper: MessagesListToViewTypedListMapper
) : TopicChatListManager(chatPaginationHelper, messagesListToViewTypedListMapper) {

    override fun sendMessage(messageText: String, topicName: String): Long {
        val message = Message.createSendingMessage(messageText, topicName)
        val viewTypedList = messagesListToViewTypedListMapper(listOf(message))
        val newElements = mutableListOf(viewTypedList[0])

        val firstTopicDelimiter =
            items.find { elem -> elem is TopicDelimiter } as? TopicDelimiter
        if (firstTopicDelimiter?.topicName != (viewTypedList[1] as TopicDelimiter).topicName)
            newElements.add(viewTypedList[1])

        val firstDate = items.find { elem -> elem is DateUi }
        if (firstDate != viewTypedList[2] as DateUi)
            newElements.add(viewTypedList[2])

        chatPaginationHelper.addNewItems(newElements)
        return message.id
    }

    override fun changeMessageTopic(messageId: Long, status: SendingStatus, topicName: String) {
        doActionWithMessage(messageId) { messageIndex ->
            val message = items[messageIndex] as MessageUi
            val oldTopic = message.topicName
            if (oldTopic != topicName) {
                val newMessage =
                    message.copy(topicName = topicName, sendingStatus = SendingStatus.SUCCESS)
                val newItems = listOf(
                    TopicDelimiter(oldTopic, uid = oldTopic),
                    newMessage,
                    TopicDelimiter(topicName, uid = topicName)
                )
                val pageIndex = chatPaginationHelper.getItemPageIndex(message)
                val page = chatPaginationHelper[pageIndex]
                val indexInPage = page.indexOf(message)
                val newPage =
                    page.subList(0, indexInPage) + newItems + page.subList(
                        indexInPage + 1,
                        page.size
                    )
                chatPaginationHelper.replacePage(newPage, pageIndex)
            }
        }
    }

}