package com.example.messengerapp.data.network.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UsersResponse(
    @field:Json(name = "members")
    val members: List<NetworkUser>
)