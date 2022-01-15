package com.example.messengerapp.domain.entity.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class UserStatus : Parcelable {
    ACTIVE, IDLE, OFFLINE
}