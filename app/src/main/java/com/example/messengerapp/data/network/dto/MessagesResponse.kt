package com.example.messengerapp.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessagesResponse(
    @field:Json(name = "messages")
    val messages: List<Message>
) {
    @JsonClass(generateAdapter = true)
    data class Message(
        @field:Json(name = "avatar_url")
        val avatarUrl: String,
        @field:Json(name = "content")
        val content: String,
        @field:Json(name = "id")
        val id: Long,
        @field:Json(name = "reactions")
        val reactions: List<Reaction>,
        @field:Json(name = "sender_email")
        val senderEmail: String,
        @field:Json(name = "sender_full_name")
        val senderFullName: String,
        @field:Json(name = "sender_id")
        val senderId: Long,
        @field:Json(name = "subject")
        val subject: String,
        @field:Json(name = "timestamp")
        val timestamp: Long,
    ) {
        @JsonClass(generateAdapter = true)
        data class Reaction(
            @field:Json(name = "emoji_code")
            val emojiCode: String,
            @field:Json(name = "emoji_name")
            val emojiName: String,
            @field:Json(name = "user_id")
            val userId: Long
        )
    }
}