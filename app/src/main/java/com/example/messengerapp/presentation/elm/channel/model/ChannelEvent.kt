package com.example.messengerapp.presentation.elm.channel.model

sealed class ChannelEvent {

    sealed class Ui : ChannelEvent() {
        data class CreateNewStream(val streamName: String, val description: String) : Ui()
        object Init : Ui()
    }

    sealed class Internal : ChannelEvent() {
        data class StreamCreated(val streamName: String) : Internal()
        data class StreamError(val error: Throwable, val streamName: String) : Internal()
    }
}
