package com.example.messengerapp.data.data_provider

import com.example.messengerapp.domain.entity.channel.Stream
import io.reactivex.rxjava3.core.Single

interface StreamsDataProvider {
    fun getStreams(isSubscribe: Boolean): Single<List<Stream>>
}