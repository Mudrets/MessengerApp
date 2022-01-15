package com.example.messengerapp.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResultResponse(
    @field:Json(name = "id")
    val id: Long = -1,
    @field:Json(name = "msg")
    val msg: String,
    @field:Json(name = "result")
    val result: String,
)
