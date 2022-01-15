package com.example.messengerapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.messengerapp.data.db.entities.EmojiDb
import io.reactivex.rxjava3.core.Single

@Dao
interface EmojiDao {

    @Query("SELECT * FROM EmojiDb")
    fun getEmojiData(): Single<List<EmojiDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmojiData(emoji: List<EmojiDb>)
}