package com.example.messengerapp.presentation.elm.profile.model

import com.example.messengerapp.domain.entity.user.UserStatus

sealed class ProfileEvent {

    sealed class Ui : ProfileEvent() {
        object Init : Ui()

        object GetStatus : Ui()
    }

    sealed class Internal : ProfileEvent() {
        data class Error(val th: Throwable) : Internal()

        data class StatusLoaded(val status: UserStatus) : Internal()
    }
}