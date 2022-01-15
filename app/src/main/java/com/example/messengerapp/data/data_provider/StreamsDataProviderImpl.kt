package com.example.messengerapp.data.data_provider

import com.example.messengerapp.data.network.ZulipApi
import com.example.messengerapp.data.network.mapper.NetworkStreamToStreamMapper
import com.example.messengerapp.data.network.mapper.NetworkSubscriptionToStreamMapper
import com.example.messengerapp.domain.entity.channel.Stream
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class StreamsDataProviderImpl @Inject constructor(
    private val zulipApi: ZulipApi,
    private val networkStreamToStreamMapper: NetworkStreamToStreamMapper,
    private val networkSubscriptionToStreamMapper: NetworkSubscriptionToStreamMapper
) : StreamsDataProvider {
    private fun getSubscribedStreams(): Single<List<Stream>> =
        zulipApi.getSubscribedStreams()
            .subscribeOn(Schedulers.io())
            .toObservable()
            .flatMapIterable { it.subscriptions }
            .concatMapSingle { stream ->
                networkSubscriptionToStreamMapper(stream)
            }
            .toList()

    private fun getStreams(): Single<List<Stream>> =
        zulipApi.getAllStreams()
            .subscribeOn(Schedulers.io())
            .toObservable()
            .flatMapIterable { it.streams }
            .concatMapSingle { stream ->
                networkStreamToStreamMapper(stream)
            }
            .toList()


    override fun getStreams(isSubscribe: Boolean): Single<List<Stream>> =
        if (isSubscribe)
            getSubscribedStreams()
        else
            getStreams()
}