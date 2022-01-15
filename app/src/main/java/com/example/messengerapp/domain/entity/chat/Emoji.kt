package com.example.messengerapp.domain.entity.chat

data class Emoji(
    val emojiName: String,
    val emojiCode: String,
    val userIds: List<Long> = listOf()
) {
    val count: Int = userIds.size
}