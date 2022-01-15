package com.example.messengerapp.data.repository

import com.example.messengerapp.data.data_provider.EmojiDataProvider
import com.example.messengerapp.data.data_store.EmojiDataStore
import com.example.messengerapp.domain.repository.EmojiRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class EmojiRepositoryImpl @Inject constructor(
    private val emojiDataProvider: EmojiDataProvider,
    private val emojiDataStore: EmojiDataStore
) : EmojiRepository {

    private var storeIsEmpty = false
    private val delay: Long
        get() = if (storeIsEmpty) SMALL_RETRY_DELAY else BIG_RETRY_DELAY

    override fun getEmojiData(): Observable<Map<String, String>> {
        val storeObservable = emojiDataStore.getEmojiData()
            .onErrorReturn { mapOf() }
            .doOnSuccess { data -> storeIsEmpty = data.isEmpty() }

        val providerObservable = emojiDataProvider.getEmojiData()
            .retryWhen { it.delay(delay, TimeUnit.SECONDS) }
            .doOnSuccess { emojiData -> emojiDataStore.insertEmojiData(emojiData) }
            .doOnError { Timber.e(it) }

        return storeObservable.mergeWith(providerObservable)
            .toObservable()
            .subscribeOn(Schedulers.computation())
    }

    companion object {
        private const val SMALL_RETRY_DELAY = 5L
        private const val BIG_RETRY_DELAY = 60L
    }
}