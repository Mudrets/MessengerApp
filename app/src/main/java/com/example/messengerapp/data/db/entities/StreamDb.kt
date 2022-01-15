package com.example.messengerapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StreamDb(
    @ColumnInfo(name = "stream_id") @PrimaryKey(autoGenerate = false) val streamId: Long,
    @ColumnInfo(name = "name") val name: String
)