package com.example.messengerapp.data.network.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubscriptionsResponse(
    @field:Json(name = "subscriptions")
    val subscriptions: List<Subscription>
) {
    @JsonClass(generateAdapter = true)
    data class Subscription(
        @field:Json(name = "name")
        val name: String,
        @field:Json(name = "stream_id")
        val streamId: Long
    )
}