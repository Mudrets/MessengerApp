package com.example.messengerapp.domain.entity.chat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class MessageType : Parcelable {
    INCOMING_MESSAGE, OUTGOING_MESSAGE
}