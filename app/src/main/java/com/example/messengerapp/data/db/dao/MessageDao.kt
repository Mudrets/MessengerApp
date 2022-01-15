package com.example.messengerapp.data.db.dao

import androidx.room.*
import com.example.messengerapp.data.db.entities.MessageDb
import com.example.messengerapp.data.db.entities.ReactionDb
import com.example.messengerapp.data.db.pojo.MessagePojo
import io.reactivex.rxjava3.core.Single

@Dao
interface MessageDao {
    @Transaction
    @Query("SELECT * FROM MessageDb WHERE messageId = :messageId")
    fun getMessage(messageId: Long): Single<MessagePojo>

    @Transaction
    @Query("SELECT * FROM MessageDb WHERE topic_name = :topicName AND stream_id = :streamId")
    fun getMessages(streamId: Long, topicName: String): Single<List<MessagePojo>>

    @Transaction
    @Query("SELECT * FROM MessageDb WHERE stream_id = :streamId")
    fun getMessagesFromStream(streamId: Long): Single<List<MessagePojo>>

    @Transaction
    fun insertAll(messagePojos: List<MessagePojo>) {
        insertMessages(messagePojos.map(MessagePojo::messageDb))
        insertReactions(messagePojos.flatMap(MessagePojo::reactions))
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessages(messageDbs: List<MessageDb>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReactions(reactionEntities: List<ReactionDb>)

    @Transaction
    @Query("DELETE FROM MessageDb WHERE messageId IN (:messageIds)")
    fun deleteAllByMessageId(messageIds: List<Long>)
}