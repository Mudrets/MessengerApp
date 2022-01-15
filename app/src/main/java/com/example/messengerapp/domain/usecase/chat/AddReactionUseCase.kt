package com.example.messengerapp.domain.usecase.chat

import com.example.messengerapp.domain.repository.ChatRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface AddReactionUseCase : (Long, String) -> Single<Boolean>

class AddReactionUseCaseImpl @Inject constructor(
    private val repository: ChatRepository
) : AddReactionUseCase {

    override fun invoke(messageId: Long, emojiName: String): Single<Boolean> =
        repository.addReactionToMessage(messageId, emojiName)

}