package com.example.messengerapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MessageDb(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "messageId") val messageId: Long,
    @ColumnInfo(name = "topic_name") val topicName: String,
    @ColumnInfo(name = "stream_id") val streamId: Long,
    @ColumnInfo(name = "sender_id") val senderId: Long,
    @ColumnInfo(name = "sender_full_name") val senderFullName: String,
    @ColumnInfo(name = "sender_email") val senderEmail: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "timestamp") val timestampSeconds: Long,
    @ColumnInfo(name = "fromMe") val fromMe: Boolean
)