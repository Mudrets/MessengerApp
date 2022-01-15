package com.example.messengerapp.presentation.elm.channel.model

sealed class ChannelEffect {
    data class ShowError(val error: Throwable, val streamName: String) : ChannelEffect()
    data class ShowSuccess(val streamName: String) : ChannelEffect()
}
