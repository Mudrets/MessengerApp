package com.example.messengerapp.presentation.elm.profile.model

sealed class ProfileCommand {
    data class GetStatus(val userId: Long) : ProfileCommand()
}