package com.example.messengerapp.presentation.models

data class ChangeMessageReactionRequest(
    val emojiName: String,
    val messageId: Long,
    val actionType: ChangeReactionType
)