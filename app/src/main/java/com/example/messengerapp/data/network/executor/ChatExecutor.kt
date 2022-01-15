package com.example.messengerapp.data.network.executor

import io.reactivex.rxjava3.core.Single

interface ChatExecutor {

    fun sendMessage(
        content: String,
        streamId: Long,
        topic: String,
        sendingId: Long
    ): Single<Long>

    fun deleteMessage(messageId: Long): Single<Boolean>

    fun addReactionToMessage(messageId: Long, emojiName: String): Single<Boolean>

    fun removeReactionMessage(messageId: Long, emojiName: String): Single<Boolean>

    fun editMessage(messageId: Long, content: String): Single<Boolean>

    fun changeTopic(messageId: Long, topicName: String): Single<Boolean>

}