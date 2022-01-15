package com.example.messengerapp.domain.repository

import com.example.messengerapp.domain.entity.chat.Message
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface ChatRepository {

    fun loadMessages(
        pageNumber: Int,
        topic: String,
        streamId: Long
    ): Observable<List<Message>>

    fun sendMessage(
        content: String,
        streamId: Long,
        topic: String,
        sendingId: Long
    ): Single<Long>

    fun addReactionToMessage(messageId: Long, emojiName: String): Single<Boolean>

    fun removeReactionMessage(messageId: Long, emojiName: String): Single<Boolean>

    fun deleteMessage(messageId: Long): Single<Boolean>

    fun editMessage(messageId: Long, content: String): Single<Boolean>

    fun changeTopic(messageId: Long, topicName: String): Single<Boolean>

}