package com.example.messengerapp.data.data_store

import com.example.messengerapp.data.data_provider.MessagesDataProvider
import com.example.messengerapp.domain.entity.chat.Message

interface MessagesDataStore : MessagesDataProvider {
    fun insertMessages(messages: List<Message>, streamId: Long, topicName: String)
    fun insertReaction(emojiName: String, messageId: Long)
    fun insertMessage(content: String, messageId: Long, streamId: Long, topicName: String)
    fun setMessages(messages: List<Message>, streamId: Long, topicName: String)
    fun deleteMessage(messageId: Long)
    fun editMessage(messageId: Long, content: String)
    fun changeTopic(messageId: Long, topicName: String)
}