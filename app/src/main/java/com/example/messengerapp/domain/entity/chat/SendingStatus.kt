package com.example.messengerapp.domain.entity.chat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class SendingStatus : Parcelable {
    IS_SENDING, SUCCESS, ERROR
}