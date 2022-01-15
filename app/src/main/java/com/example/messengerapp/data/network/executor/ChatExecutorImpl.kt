package com.example.messengerapp.data.network.executor

import com.example.messengerapp.data.network.ZulipApi
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ChatExecutorImpl @Inject constructor(
    private val zulipApi: ZulipApi
) : ChatExecutor {
    override fun sendMessage(
        content: String,
        streamId: Long,
        topic: String,
        sendingId: Long
    ): Single<Long> =
        zulipApi.sendMessage(
            content = content,
            streamId = streamId,
            topic = topic
        )
            .subscribeOn(Schedulers.io())
            .map { response ->
                if (response.result == "success")
                    response.id
                else
                    throw Exception(response.msg)
            }

    override fun deleteMessage(messageId: Long): Single<Boolean> =
        zulipApi.deleteMessage(messageId)
            .subscribeOn(Schedulers.io())
            .map { response ->
                if (response.msg == "You don't have permission to delete this message")
                    throw IllegalAccessException(response.msg)
                response.result == "success"
            }

    override fun addReactionToMessage(messageId: Long, emojiName: String): Single<Boolean> =
        zulipApi.addReactionToMessage(
            messageId = messageId,
            emojiName = emojiName
        )
            .subscribeOn(Schedulers.io())
            .map { response -> response.result == "success" }


    override fun removeReactionMessage(messageId: Long, emojiName: String): Single<Boolean> =
        zulipApi.removeReactionFromMessage(
            messageId = messageId,
            emojiName = emojiName
        )
            .subscribeOn(Schedulers.io())
            .map { response -> response.result == "success" }

    override fun editMessage(messageId: Long, content: String): Single<Boolean> =
        zulipApi.editMessage(messageId, content)
            .subscribeOn(Schedulers.io())
            .map { response -> response.result == "success" }

    override fun changeTopic(messageId: Long, topicName: String): Single<Boolean> =
        zulipApi.changeTopic(messageId, topicName)
            .subscribeOn(Schedulers.io())
            .map { response -> response.result == "success" }
}