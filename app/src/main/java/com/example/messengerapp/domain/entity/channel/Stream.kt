package com.example.messengerapp.domain.entity.channel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Stream(
    val name: String,
    val topics: List<Topic>,
    val streamId: Long
) : Parcelable