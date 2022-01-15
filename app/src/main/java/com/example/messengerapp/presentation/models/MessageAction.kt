package com.example.messengerapp.presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class MessageAction : Parcelable {
    EDIT, COPY, ADD_REACTION, DELETE, CHANGE_TOPIC
}