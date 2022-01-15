package com.example.messengerapp.data.network.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserPresenceResponse(
    @field:Json(name = "presence")
    val presence: Presence,
) {
    @JsonClass(generateAdapter = true)
    data class Presence(
        @field:Json(name = "aggregated")
        val aggregated: Aggregated? = null,
        @field:Json(name = "website")
        val website: Website? = null,
        @field:Json(name = "ZulipMobile")
        val zulipMobile: ZulipMobile? = null,
        @field:Json(name = "ZulipTerminal")
        val zulipTerminal: ZulipTerminal? = null,
        @field:Json(name = "ZulipDesktop")
        val zulipDesktop: ZulipDesktop? = null
    ) {
        @JsonClass(generateAdapter = true)
        data class Aggregated(
            @field:Json(name = "status")
            val status: String,
            @field:Json(name = "timestamp")
            val timestamp: Int
        )

        @JsonClass(generateAdapter = true)
        data class Website(
            @field:Json(name = "status")
            val status: String,
            @field:Json(name = "timestamp")
            val timestamp: Int
        )

        @JsonClass(generateAdapter = true)
        data class ZulipMobile(
            @field:Json(name = "status")
            val status: String,
            @field:Json(name = "timestamp")
            val timestamp: Int
        )

        @JsonClass(generateAdapter = true)
        data class ZulipTerminal(
            @field:Json(name = "status")
            val status: String,
            @field:Json(name = "timestamp")
            val timestamp: Int
        )

        @JsonClass(generateAdapter = true)
        data class ZulipDesktop(
            @field:Json(name = "status")
            val status: String,
            @field:Json(name = "timestamp")
            val timestamp: Int
        )
    }
}