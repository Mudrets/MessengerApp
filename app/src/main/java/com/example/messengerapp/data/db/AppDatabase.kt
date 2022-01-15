package com.example.messengerapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.messengerapp.data.db.dao.EmojiDao
import com.example.messengerapp.data.db.dao.MessageDao
import com.example.messengerapp.data.db.dao.StreamDao
import com.example.messengerapp.data.db.dao.UserDao
import com.example.messengerapp.data.db.entities.*

@Database(
    entities = [MessageDb::class, ReactionDb::class, UserDb::class,
        StreamDb::class, TopicDb::class, StreamPageDb::class, EmojiDb::class], version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun streamDao(): StreamDao
    abstract fun userDao(): UserDao
    abstract fun emojiDao(): EmojiDao
}