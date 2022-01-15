package com.example.messengerapp.data.repository

import com.example.messengerapp.data.data_provider.MessagesDataProvider
import com.example.messengerapp.data.data_store.MessagesDataStore
import com.example.messengerapp.data.network.executor.ChatExecutor
import com.example.messengerapp.domain.entity.chat.Message
import com.example.messengerapp.domain.repository.ChatRepository
import com.example.messengerapp.util.ext.retryWithDelay
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val messagesDataProvider: MessagesDataProvider,
    private val messagesDataStore: MessagesDataStore,
    private val chatExecutor: ChatExecutor
) : ChatRepository {

    private var nextPageAnchor: Long = ANCHOR_NEW
    private val pageToAnchorMap: MutableMap<Int, Long> = mutableMapOf()

    private fun pageToAnchor(page: Int): Long = when {
        page == 0 -> {
            pageToAnchorMap.clear()
            nextPageAnchor = ANCHOR_NEW
            pageToAnchorMap[page] = nextPageAnchor
            ANCHOR_NEW
        }
        pageToAnchorMap.containsKey(page) -> {
            pageToAnchorMap[page]!!
        }
        else -> {
            pageToAnchorMap[page] = nextPageAnchor
            nextPageAnchor
        }
    }

    override fun loadMessages(
        pageNumber: Int,
        topic: String,
        streamId: Long
    ): Observable<List<Message>> =
        loadMessages(pageToAnchor(pageNumber), topic, streamId)

    private fun getNextPageAnchor(messages: List<Message>): Long {
        val lastMessage = messages.minByOrNull { message -> message.date.time }
        return lastMessage?.id ?: nextPageAnchor
    }

    private fun getDataFromDataStore(
        anchor: Long,
        topic: String,
        streamId: Long
    ): Single<List<Message>>? {
        return if (anchor == ANCHOR_NEW) {
            messagesDataStore.getMessages(anchor, PAGE_SIZE + 1, 0, topic, streamId)
                .onErrorReturn { listOf() }
                .doOnSuccess { messages ->
                    nextPageAnchor = getNextPageAnchor(messages)
                    messages.filter { message -> message.id != nextPageAnchor }
                }
        } else {
            null
        }
    }

    private fun getDataFromDataProvider(
        anchor: Long,
        topic: String,
        streamId: Long
    ): Single<List<Message>> {
        val providerObservable =
            messagesDataProvider.getMessages(anchor, PAGE_SIZE + 1, 0, topic, streamId)
                .doOnSuccess { messages ->
                    if (anchor == ANCHOR_NEW)
                        messagesDataStore.setMessages(messages, streamId, topic)
                    nextPageAnchor = getNextPageAnchor(messages)
                }

        return if (anchor == ANCHOR_NEW)
            providerObservable
                .doOnSuccess { messages ->
                    messagesDataStore.setMessages(messages, streamId, topic)
                    nextPageAnchor = getNextPageAnchor(messages)
                }
                .retryWithDelay(TIMES, RETRY_DELAY)
        else
            providerObservable
                .retryWhen { it.delay(RETRY_DELAY, TimeUnit.SECONDS) }
                .doOnSuccess { messages -> nextPageAnchor = getNextPageAnchor(messages) }
    }

    private fun getMessages(
        anchor: Long,
        topic: String,
        streamId: Long
    ): Observable<List<Message>> {
        val dbObservable = getDataFromDataStore(anchor, topic, streamId)
        val serverObservable = getDataFromDataProvider(anchor, topic, streamId)
            .doOnError { Timber.e(it) }

        return if (dbObservable != null)
            dbObservable.mergeWith(serverObservable).toObservable()
        else
            serverObservable.toObservable()
    }

    private fun loadMessages(
        anchor: Long,
        topic: String,
        streamId: Long
    ): Observable<List<Message>> = getMessages(anchor, topic, streamId)
        .subscribeOn(Schedulers.computation())
        .doOnNext { Timber.e(it.toString()) }

    override fun sendMessage(
        content: String,
        streamId: Long,
        topic: String,
        sendingId: Long
    ): Single<Long> =
        chatExecutor.sendMessage(content, streamId, topic, sendingId)
            .doOnSuccess { messageId ->
                if (messageId > -1) {
                    try {
                        messagesDataStore.insertMessage(content, messageId, streamId, topic)
                    } catch (ex: Exception) {
                        Timber.e(ex)
                    }
                }
            }

    override fun addReactionToMessage(
        messageId: Long,
        emojiName: String
    ): Single<Boolean> =
        chatExecutor.addReactionToMessage(messageId, emojiName)
            .doOnSuccess { isSuccess ->
                if (isSuccess) {
                    try {
                        messagesDataStore.insertReaction(emojiName, messageId)
                    } catch (ex: Exception) {
                        Timber.e(ex)
                    }
                }
            }


    override fun removeReactionMessage(
        messageId: Long,
        emojiName: String
    ): Single<Boolean> =
        chatExecutor.removeReactionMessage(messageId, emojiName)

    override fun deleteMessage(messageId: Long): Single<Boolean> =
        chatExecutor.deleteMessage(messageId)
            .doOnSuccess { isSuccess ->
                if (isSuccess)
                    messagesDataStore.deleteMessage(messageId)
            }

    override fun editMessage(messageId: Long, content: String): Single<Boolean> =
        chatExecutor.editMessage(messageId, content)
            .doOnSuccess { isSuccess ->
                if (isSuccess)
                    try {
                        messagesDataStore.editMessage(messageId, content)
                    } catch (ex: Exception) {
                        Timber.e(ex)
                    }
            }

    override fun changeTopic(messageId: Long, topicName: String): Single<Boolean> =
        chatExecutor.changeTopic(messageId, topicName)
            .doOnSuccess { isSuccess ->
                if (isSuccess)
                    try {
                        messagesDataStore.changeTopic(messageId, topicName)
                    } catch (ex: Exception) {
                        Timber.e(ex)
                    }
            }


    companion object {
        private const val ANCHOR_NEW = 10000000000000000L
        private const val PAGE_SIZE = 20L
        private const val RETRY_DELAY = 3L
        private const val TIMES = 5
    }

}
