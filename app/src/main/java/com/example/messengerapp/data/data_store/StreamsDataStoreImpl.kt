package com.example.messengerapp.data.data_store

import com.example.messengerapp.data.db.dao.StreamDao
import com.example.messengerapp.data.db.entities.StreamPageDb
import com.example.messengerapp.data.db.mapper.from_db.StreamPojoToStreamMapper
import com.example.messengerapp.data.db.mapper.to_db.StreamToStreamPojoMapper
import com.example.messengerapp.data.db.pojo.StreamPojo
import com.example.messengerapp.domain.entity.channel.Stream
import com.example.messengerapp.util.Constants
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class StreamsDataStoreImpl @Inject constructor(
    private val streamDao: StreamDao,
    private val streamPojoToStreamMapper: StreamPojoToStreamMapper,
    private val streamToStreamPojoMapper: StreamToStreamPojoMapper
) : StreamsDataStore {

    private fun getStreamsPojos(): Observable<StreamPojo> =
        streamDao.getAll()
            .toObservable()
            .subscribeOn(Schedulers.io())
            .flatMapIterable { it }

    private fun getSubscribedStreams(): Single<List<Stream>> =
        getStreamsPojos()
            .filter { streamPojo ->
                streamPojo.streamPageDbs.find { it.name == Constants.SUBSCRIBED } != null
            }
            .map(streamPojoToStreamMapper)
            .toList()

    private fun getStreams(): Single<List<Stream>> =
        getStreamsPojos()
            .map(streamPojoToStreamMapper)
            .toList()

    override fun getStreams(isSubscribe: Boolean): Single<List<Stream>> =
        if (isSubscribe)
            getSubscribedStreams()
        else
            getStreams()

    override fun insertStreams(streams: List<Stream>, isSubscribe: Boolean) {
        streamDao.insertAll(streams.map { stream ->
            streamToStreamPojoMapper(stream, isSubscribe)
        })
    }

    override fun setStreams(streams: List<Stream>, isSubscribe: Boolean) {
        val streamPageName = if (isSubscribe) Constants.SUBSCRIBED else Constants.ALL_STREAMS
        streamDao.getAll()
            .toObservable()
            .flatMapIterable { it }
            .filter {
                it.streamPageDbs.isEmpty()
            }
            .filter {
                it.streamPageDbs
                    .map(StreamPageDb::name)
                    .contains(streamPageName)
            }
            .map { it.streamDb.streamId }
            .toList()
            .doAfterSuccess {
                streamDao.insertAll(
                    streams.map { stream ->
                        streamToStreamPojoMapper(stream, isSubscribe)
                    }
                )
            }
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = { list ->
                    streamDao.removeByIdWithName(list, streamPageName)
                },
                onError = {
                    Timber.e(it)
                }
            )
    }
}