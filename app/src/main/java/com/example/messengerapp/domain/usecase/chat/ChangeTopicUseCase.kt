package com.example.messengerapp.domain.usecase.chat

import com.example.messengerapp.domain.repository.ChatRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface ChangeTopicUseCase : (Long, String) -> Single<Boolean>

class ChangeTopicUseCaseImpl @Inject constructor(
    private val chatRepository: ChatRepository
) : ChangeTopicUseCase {

    override fun invoke(messageId: Long, topicName: String): Single<Boolean> =
        chatRepository.changeTopic(messageId, topicName)

}