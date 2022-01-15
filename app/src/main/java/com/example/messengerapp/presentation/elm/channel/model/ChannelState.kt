package com.example.messengerapp.presentation.elm.channel.model

sealed class ChannelState {
    object Normal : ChannelState()
    object Loading : ChannelState()
}
