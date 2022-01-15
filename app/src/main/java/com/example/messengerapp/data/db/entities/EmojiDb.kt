package com.example.messengerapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EmojiDb(
    @PrimaryKey @ColumnInfo(name = "emoji_name") val emojiName: String,
    @ColumnInfo(name = "emoji_code") val emojiCode: String
)