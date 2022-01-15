package com.example.messengerapp.data.repository

import com.example.messengerapp.data.data_provider.StreamsDataProvider
import com.example.messengerapp.data.data_store.StreamsDataStore
import com.example.messengerapp.data.network.executor.ChannelExecutor
import com.example.messengerapp.domain.entity.channel.Stream
import com.example.messengerapp.domain.repository.ChannelRepository
import com.example.messengerapp.util.ext.retryWithDelay
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


class ChannelRepositoryImpl @Inject constructor(
    private val streamsDataProvider: StreamsDataProvider,
    private val streamsDataStore: StreamsDataStore,
    private val channelExecutor: ChannelExecutor
) : ChannelRepository {

    private fun loadStreams(isSubscribed: Boolean): Observable<List<Stream>> {
        val dbObservable = streamsDataStore.getStreams(isSubscribed)
            .onErrorReturn { listOf() }
        val serverObservable = streamsDataProvider.getStreams(isSubscribed)
            .retryWithDelay(TIMES, RETRY_DELAY)
            .doOnSuccess { streams -> streamsDataStore.setStreams(streams, isSubscribed) }
            .doOnError { Timber.e(it) }
        return dbObservable.mergeWith(serverObservable)
            .toObservable()
            .subscribeOn(Schedulers.computation())
    }

    override fun loadAllStreams(): Observable<List<Stream>> = loadStreams(false)

    override fun loadSubscribedStreams(): Observable<List<Stream>> = loadStreams(true)

    override fun createNewStream(streamName: String, description: String): Single<Boolean> =
        channelExecutor.createNewStream(streamName, description)

    companion object {
        private const val RETRY_DELAY = 3L
        private const val TIMES = 3
    }

}