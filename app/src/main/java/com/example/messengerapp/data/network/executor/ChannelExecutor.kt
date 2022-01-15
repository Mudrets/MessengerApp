package com.example.messengerapp.data.network.executor

import io.reactivex.rxjava3.core.Single

interface ChannelExecutor {
    fun createNewStream(streamName: String, description: String): Single<Boolean>
}