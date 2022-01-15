package com.example.messengerapp.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Subscription(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "description")
    val description: String
)