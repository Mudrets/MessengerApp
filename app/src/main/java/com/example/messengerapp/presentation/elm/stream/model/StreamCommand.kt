package com.example.messengerapp.presentation.elm.stream.model

sealed class StreamCommand {
    data class SearchStreams(
        val searchQuery: String,
        val isSubscribed: Boolean
    ) : StreamCommand()
}