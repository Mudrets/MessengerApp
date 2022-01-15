package com.example.messengerapp.domain.entity.chat

import java.util.*

data class Message(
    val id: Long,
    val textMessage: String = "",
    val senderFullName: String = "",
    val avatarUrl: String = "",
    val date: Date = Date(),
    val emojiList: List<Emoji> = listOf(),
    val sendingStatus: SendingStatus = SendingStatus.SUCCESS,
    val senderId: Long = 0,
    val isMyMessage: Boolean,
    val senderEmail: String,
    val topicName: String = ""
) {

    companion object {

        private var notSentMessageId = -1L

        fun createSendingMessage(messageText: String, topicName: String): Message {
            return Message(
                id = notSentMessageId--,
                textMessage = messageText,
                isMyMessage = true,
                sendingStatus = SendingStatus.IS_SENDING,
                senderEmail = "",
                topicName = topicName
            )
        }

    }

}