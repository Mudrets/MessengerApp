package com.example.messengerapp.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkUser(
    @field:Json(name = "avatar_url")
    val avatarUrl: String,
    @field:Json(name = "email")
    val email: String,
    @field:Json(name = "full_name")
    val fullName: String,
    @field:Json(name = "is_active")
    val isActive: Boolean,
    @field:Json(name = "is_bot")
    val isBot: Boolean,
    @field:Json(name = "user_id")
    val userId: Long
)