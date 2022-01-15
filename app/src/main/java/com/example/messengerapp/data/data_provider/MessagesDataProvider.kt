package com.example.messengerapp.data.data_provider

import com.example.messengerapp.domain.entity.chat.Message
import io.reactivex.rxjava3.core.Single

interface MessagesDataProvider {
    fun getMessages(
        anchor: Long,
        numBefore: Long,
        numAfter: Long,
        topic: String,
        streamId: Long
    ): Single<List<Message>>
}