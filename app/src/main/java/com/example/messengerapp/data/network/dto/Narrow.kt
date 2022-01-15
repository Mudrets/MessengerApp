package com.example.messengerapp.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Narrow(
    @field:Json(name = "operator")
    val operator: String,
    @field:Json(name = "operand")
    val operand: Any
)