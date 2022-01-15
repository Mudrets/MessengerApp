package com.example.messengerapp.domain.entity.channel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Topic(
    val name: String,
    val maxId: Long
) : Parcelable
