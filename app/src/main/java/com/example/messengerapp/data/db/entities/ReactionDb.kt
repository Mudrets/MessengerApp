package com.example.messengerapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = MessageDb::class,
        parentColumns = arrayOf("messageId"),
        childColumns = arrayOf("messageId"),
        onDelete = ForeignKey.CASCADE
    )],
)
data class ReactionDb(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    @ColumnInfo(name = "messageId") val messageId: Long,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "emojiName") val emojiName: String,
    @ColumnInfo(name = "emojiCode") val emojiCode: String
)