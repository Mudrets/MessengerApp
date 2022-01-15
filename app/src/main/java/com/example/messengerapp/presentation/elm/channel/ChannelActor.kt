package com.example.messengerapp.presentation.elm.channel

import com.example.messengerapp.domain.usecase.channel.CreateNewStreamUseCase
import com.example.messengerapp.presentation.elm.channel.model.ChannelCommand
import com.example.messengerapp.presentation.elm.channel.model.ChannelEvent
import io.reactivex.rxjava3.core.Observable
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class ChannelActor @Inject constructor(
    private val createStreamUseCase: CreateNewStreamUseCase
) : Actor<ChannelCommand, ChannelEvent> {

    override fun execute(command: ChannelCommand): Observable<ChannelEvent> = when (command) {
        is ChannelCommand.CreateStream -> {
            createStreamUseCase(command.streamName, command.description)
                .mapEvents(
                    { resultFlag ->
                        if (resultFlag)
                            ChannelEvent.Internal.StreamCreated(command.streamName)
                        else
                            ChannelEvent.Internal.StreamError(Throwable(), command.streamName)
                    },
                    { error -> ChannelEvent.Internal.StreamError(error, command.streamName) }
                )
        }
    }
}