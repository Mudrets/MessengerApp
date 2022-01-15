package com.example.messengerapp.domain.usecase.chat

import com.example.messengerapp.domain.entity.chat.Message
import com.example.messengerapp.domain.repository.ChatRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

interface GetMessagesUseCase : (Int, String, Long) -> Observable<List<Message>>

class GetMessagesUseCaseImpl @Inject constructor(
    private val repository: ChatRepository
) : GetMessagesUseCase {

    override fun invoke(
        page: Int,
        topic: String,
        streamId: Long
    ): Observable<List<Message>> =
        repository.loadMessages(page, topic, streamId)

}