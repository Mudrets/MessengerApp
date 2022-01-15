package com.example.messengerapp.presentation.elm.stream.model

import com.example.messengerapp.presentation.recyclerview.stream.StreamUi

sealed class StreamEvent {
    sealed class Ui : StreamEvent() {
        data class OpenChat(
            val streamName: String,
            val streamId: Long,
            val topicName: String = ""
        ) : Ui()

        data class SearchStreams(
            val searchQuery: String,
            val isSubscribe: Boolean
        ) : Ui()

        object Init : Ui()
    }

    sealed class Internal : StreamEvent() {
        data class ErrorSearching(val th: Throwable) : StreamEvent.Internal()
        data class StreamLoaded(
            val streams: List<StreamUi>
        ) : StreamEvent.Internal()

        object Nothing : Internal()

        object ShowLoad : Internal()
    }
}