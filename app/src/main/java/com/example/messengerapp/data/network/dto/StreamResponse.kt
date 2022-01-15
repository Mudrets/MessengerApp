package com.example.messengerapp.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StreamResponse(
    @field:Json(name = "streams")
    val streams: List<Stream>
) {
    @JsonClass(generateAdapter = true)
    data class Stream(
        @field:Json(name = "name")
        val name: String,
        @field:Json(name = "stream_id")
        val streamId: Long
    )
}