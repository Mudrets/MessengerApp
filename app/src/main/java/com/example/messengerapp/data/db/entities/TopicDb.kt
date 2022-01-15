package com.example.messengerapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = StreamDb::class,
        parentColumns = arrayOf("stream_id"),
        childColumns = arrayOf("stream_id"),
        onDelete = ForeignKey.CASCADE
    )],
)
data class TopicDb(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "stream_id") val streamId: Long,
    @ColumnInfo(name = "max_id") val maxId: Long
)