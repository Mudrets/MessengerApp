package com.example.messengerapp.presentation.elm.profile.model

sealed class ProfileEffect {
    data class ShowError(val th: Throwable) : ProfileEffect()
}