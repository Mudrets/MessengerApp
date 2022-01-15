package com.example.messengerapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class UserDb(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = false) val id: Long,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "full_name") val fullName: String
)