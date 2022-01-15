package com.example.messengerapp.data.network.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubscribeChannelResponse(
    @Json(name = "already_subscribed")
    val alreadySubscribed: Map<String, List<String>>,
    @Json(name = "result")
    val result: String,
)