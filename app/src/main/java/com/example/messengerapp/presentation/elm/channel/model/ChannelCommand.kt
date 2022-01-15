package com.example.messengerapp.presentation.elm.channel.model

sealed class ChannelCommand {
    data class CreateStream(val streamName: String, val description: String) : ChannelCommand()
}