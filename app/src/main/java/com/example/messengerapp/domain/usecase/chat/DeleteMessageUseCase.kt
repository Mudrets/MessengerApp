package com.example.messengerapp.domain.usecase.chat

import com.example.messengerapp.domain.repository.ChatRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface DeleteMessageUseCase : (Long) -> Single<Boolean>

class DeleteMessageUseCaseImpl @Inject constructor(
    private val chatRepository: ChatRepository
) : DeleteMessageUseCase {

    override fun invoke(messageId: Long): Single<Boolean> = chatRepository.deleteMessage(messageId)

}