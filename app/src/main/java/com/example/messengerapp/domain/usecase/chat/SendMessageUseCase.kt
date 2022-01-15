package com.example.messengerapp.domain.usecase.chat

import com.example.messengerapp.domain.repository.ChatRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface SendMessageUseCase : (String, Long, String, Long) -> Single<Long>

class SendMessageUseCaseImpl @Inject constructor(
    private val repository: ChatRepository
) : SendMessageUseCase {

    override fun invoke(
        content: String,
        streamId: Long,
        topic: String,
        sendingId: Long
    ): Single<Long> =
        repository.sendMessage(content, streamId, topic, sendingId)

}