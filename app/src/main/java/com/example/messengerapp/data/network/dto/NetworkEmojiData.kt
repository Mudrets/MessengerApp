package com.example.messengerapp.data.network.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkEmojiData(
    @Json(name = "name_to_codepoint")
    val nameToCodepoint: Map<String, String>,
    @Json(name = "names")
    val names: List<String>
)