package com.example.messengerapp.presentation.elm.stream.model

import com.example.messengerapp.presentation.recyclerview.stream.StreamUi

sealed class StreamState {
    object Loading : StreamState()
    data class Error(val th: Throwable) : StreamState()
    data class Success(val streams: List<StreamUi>) : StreamState()
}