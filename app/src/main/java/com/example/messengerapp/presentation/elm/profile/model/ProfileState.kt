package com.example.messengerapp.presentation.elm.profile.model

import com.example.messengerapp.domain.entity.user.UserStatus

sealed class ProfileState {

    data class Success(val status: UserStatus) : ProfileState()

    object Loading : ProfileState()

    data class Error(val th: Throwable) : ProfileState()
}