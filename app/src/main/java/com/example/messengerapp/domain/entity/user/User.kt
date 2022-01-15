package com.example.messengerapp.domain.entity.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val fullName: String,
    val email: String,
    val avatarUrl: String = "",
    val status: UserStatus = UserStatus.OFFLINE,
    val userId: Long,
) : Parcelable