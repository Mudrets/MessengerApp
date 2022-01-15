package com.example.messengerapp.presentation.elm.stream

import com.example.messengerapp.presentation.elm.stream.model.StreamCommand
import com.example.messengerapp.presentation.elm.stream.model.StreamEffect
import com.example.messengerapp.presentation.elm.stream.model.StreamEvent
import com.example.messengerapp.presentation.elm.stream.model.StreamState
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class StreamReducer(
    private val isSubscribed: Boolean
) : DslReducer<StreamEvent, StreamState, StreamEffect, StreamCommand>() {
    override fun Result.reduce(event: StreamEvent): Any = when (event) {
        is StreamEvent.Internal.ErrorSearching -> {
            state { StreamState.Error(event.th) }
        }
        is StreamEvent.Internal.StreamLoaded -> {
            state { StreamState.Success(event.streams) }
        }
        is StreamEvent.Ui.OpenChat -> {
            effects {
                +StreamEffect.OpenChat(
                    event.streamName,
                    event.streamId,
                    event.topicName
                )
            }
        }
        is StreamEvent.Ui.SearchStreams -> {
            if ((state as? StreamState.Success)?.streams.isNullOrEmpty())
                state { StreamState.Loading }
            commands { +StreamCommand.SearchStreams(event.searchQuery, isSubscribed) }
        }
        StreamEvent.Ui.Init -> {
            state { StreamState.Loading }
        }
        StreamEvent.Internal.ShowLoad -> {
            state { StreamState.Loading }
        }
        StreamEvent.Internal.Nothing -> {
        }
    }
}