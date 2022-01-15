package com.example.messengerapp.domain.repository

import com.example.messengerapp.domain.entity.channel.Stream
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface ChannelRepository {

    fun loadAllStreams(): Observable<List<Stream>>

    fun loadSubscribedStreams(): Observable<List<Stream>>

    fun createNewStream(streamName: String, description: String): Single<Boolean>

}