package com.example.messengerapp.presentation.elm.channel

import com.example.messengerapp.presentation.elm.channel.model.ChannelCommand
import com.example.messengerapp.presentation.elm.channel.model.ChannelEffect
import com.example.messengerapp.presentation.elm.channel.model.ChannelEvent
import com.example.messengerapp.presentation.elm.channel.model.ChannelState
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

class ChannelReducer @Inject constructor() :
    DslReducer<ChannelEvent, ChannelState, ChannelEffect, ChannelCommand>() {

    override fun Result.reduce(event: ChannelEvent): Any = when (event) {
        is ChannelEvent.Internal.StreamError -> {
            state { ChannelState.Normal }
            effects { +ChannelEffect.ShowError(event.error, event.streamName) }
        }
        is ChannelEvent.Internal.StreamCreated -> {
            state { ChannelState.Normal }
            effects { +ChannelEffect.ShowSuccess(event.streamName) }
        }
        is ChannelEvent.Ui.CreateNewStream -> {
            state { ChannelState.Loading }
            commands { +ChannelCommand.CreateStream(event.streamName, event.description) }
        }
        ChannelEvent.Ui.Init -> {
        }
    }
}