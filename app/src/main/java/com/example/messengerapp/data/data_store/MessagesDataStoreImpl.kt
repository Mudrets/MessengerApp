package com.example.messengerapp.data.data_store

import com.example.messengerapp.EmojiInfo
import com.example.messengerapp.data.db.dao.MessageDao
import com.example.messengerapp.data.db.mapper.from_db.MessagePojoToMessageMapper
import com.example.messengerapp.data.db.mapper.to_db.EmojiListToReactionDbListMapper
import com.example.messengerapp.data.db.mapper.to_db.MessageToMessagePojoMapper
import com.example.messengerapp.domain.entity.chat.Emoji
import com.example.messengerapp.domain.entity.chat.Message
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class MessagesDataStoreImpl @Inject constructor(
    private val messageDao: MessageDao,
    private val messageToMessagePojoMapper: MessageToMessagePojoMapper,
    private val messagePojoToMessageMapper: MessagePojoToMessageMapper,
    private val emojiListToReactionDbListMapper: EmojiListToReactionDbListMapper,
    private val authorizedUserId: Provider<Long>,
    private val emojiInfo: EmojiInfo
) : MessagesDataStore {
    override fun insertMessages(messages: List<Message>, streamId: Long, topicName: String) {
        messageDao.insertAll(messages.map { message ->
            messageToMessagePojoMapper(message, topicName, streamId)
        })
    }

    override fun insertReaction(emojiName: String, messageId: Long) {
        messageDao.insertReactions(
            emojiListToReactionDbListMapper(
                listOf(
                    Emoji(
                        emojiName = emojiName,
                        emojiCode = emojiInfo.getEmojiCode(emojiName),
                        userIds = listOf(authorizedUserId.get())
                    )
                ), messageId
            )
        )
    }

    override fun insertMessage(
        content: String,
        messageId: Long,
        streamId: Long,
        topicName: String
    ) {
        insertMessages(
            listOf(
                Message(
                    textMessage = content,
                    id = messageId,
                    isMyMessage = true,
                    senderEmail = ""
                )
            ),
            streamId,
            topicName
        )
    }

    override fun setMessages(messages: List<Message>, streamId: Long, topicName: String) {
        getMessages(0, 0, 0, topicName, streamId)
            .subscribeOn(Schedulers.io())
            .toObservable()
            .flatMapIterable { it }
            .filter { message -> !messages.contains(message) }
            .map { message -> message.id }
            .toList()
            .doOnSuccess {
                insertMessages(messages, streamId, topicName)
            }
            .subscribeBy(
                onSuccess = { ids ->
                    messageDao.deleteAllByMessageId(ids)
                },
                onError = { error ->
                    Timber.e(error)
                }
            )
    }

    override fun deleteMessage(messageId: Long) {
        messageDao.deleteAllByMessageId(listOf(messageId))
    }

    override fun editMessage(messageId: Long, content: String) {
        messageDao.getMessage(messageId)
            .subscribeOn(Schedulers.io())
            .map(messagePojoToMessageMapper)
            .subscribeBy { message ->
                insertMessage(
                    content = content,
                    messageId = messageId,
                    streamId = message.senderId,
                    topicName = message.topicName
                )
            }.dispose()

    }

    override fun changeTopic(messageId: Long, topicName: String) {
        messageDao.getMessage(messageId)
            .subscribeOn(Schedulers.io())
            .map(messagePojoToMessageMapper)
            .subscribeBy { message ->
                insertMessage(
                    content = message.textMessage,
                    messageId = messageId,
                    streamId = message.senderId,
                    topicName = topicName
                )
            }.dispose()
    }

    override fun getMessages(
        anchor: Long,
        numBefore: Long,
        numAfter: Long,
        topic: String,
        streamId: Long
    ): Single<List<Message>> {
        val messagesSingle = if (topic.isBlank())
            messageDao.getMessagesFromStream(streamId)
        else
            messageDao.getMessages(streamId, topic)
        return messagesSingle
            .subscribeOn(Schedulers.io())
            .map { messagePojos ->
                messagePojos.map(messagePojoToMessageMapper)
            }
    }

}