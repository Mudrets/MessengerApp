package com.example.messengerapp.presentation.elm.chat.model

sealed class ChatEffect {
    data class ErrorEffect(val th: Throwable) : ChatEffect()
}