package com.example.messengerapp.data.data_store

import com.example.messengerapp.data.data_provider.StreamsDataProvider
import com.example.messengerapp.domain.entity.channel.Stream

interface StreamsDataStore : StreamsDataProvider {
    fun insertStreams(streams: List<Stream>, isSubscribe: Boolean)
    fun setStreams(streams: List<Stream>, isSubscribe: Boolean)
}