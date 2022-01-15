package com.example.messengerapp.domain.usecase.chat

import com.example.messengerapp.domain.repository.ChatRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface RemoveReactionUseCase : (Long, String) -> Single<Boolean>

class RemoveReactionUseCaseImpl @Inject constructor(
    private val repository: ChatRepository
) : RemoveReactionUseCase {

    override fun invoke(messageId: Long, emojiName: String): Single<Boolean> =
        repository.removeReactionMessage(messageId, emojiName)

}