package com.example.messengerapp.domain.usecase.channel

import com.example.messengerapp.domain.repository.ChannelRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface CreateNewStreamUseCase : (String, String) -> Single<Boolean>

class CreateNewStreamUseCaseImpl @Inject constructor(
    private val channelRepository: ChannelRepository
) : CreateNewStreamUseCase {

    override fun invoke(streamName: String, description: String): Single<Boolean> =
        channelRepository.createNewStream(streamName, description)

}