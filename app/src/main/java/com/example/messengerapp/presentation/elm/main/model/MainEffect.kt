package com.example.messengerapp.presentation.elm.main.model

sealed class MainEffect {
    data class ShowError(val th: Throwable) : MainEffect()
}