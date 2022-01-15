package com.example.messengerapp.domain.usecase.chat

import com.example.messengerapp.domain.repository.ChatRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface EditMessageUseCase : (Long, String) -> Single<Boolean>

class EditMessageUseCaseImpl @Inject constructor(
    private val chatRepository: ChatRepository
) : EditMessageUseCase {

    override fun invoke(messageId: Long, content: String): Single<Boolean> =
        chatRepository.editMessage(messageId, content)

}