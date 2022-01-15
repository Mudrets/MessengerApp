package com.example.messengerapp.presentation.elm.stream.model

sealed class StreamEffect {
    data class Error(val th: Throwable) : StreamEffect()
    data class OpenChat(
        val streamName: String,
        val streamId: Long,
        val topicName: String = ""
    ) : StreamEffect()
}