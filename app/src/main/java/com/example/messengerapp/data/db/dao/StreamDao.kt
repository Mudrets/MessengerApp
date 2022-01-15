package com.example.messengerapp.data.db.dao

import androidx.room.*
import com.example.messengerapp.data.db.entities.StreamDb
import com.example.messengerapp.data.db.entities.StreamPageDb
import com.example.messengerapp.data.db.entities.TopicDb
import com.example.messengerapp.data.db.pojo.StreamPojo
import io.reactivex.rxjava3.core.Single

@Dao
interface StreamDao {
    @Transaction
    fun insertAll(streamPojos: List<StreamPojo>) {
        insertStreams(streamPojos.map(StreamPojo::streamDb))
        insertTopics(streamPojos.flatMap(StreamPojo::topics))
        insertPages(streamPojos.flatMap(StreamPojo::streamPageDbs))
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStreams(streamDbs: List<StreamDb>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPages(streamPageDbs: List<StreamPageDb>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTopics(topicsDbs: List<TopicDb>)

    @Transaction
    @Query("SELECT * FROM StreamDb")
    fun getAll(): Single<List<StreamPojo>>

    @Query("SELECT * FROM StreamDb WHERE stream_id = :streamId")
    fun getStreamById(streamId: Long): Single<StreamPojo>

    @Transaction
    fun removeByIdWithName(ids: List<Long>, name: String) {
        removeAllPagesByName(name)
        removeAllStreamsById(ids)
    }

    @Query("DELETE FROM StreamDb WHERE stream_id IN (:ids)")
    fun removeAllStreamsById(ids: List<Long>)

    @Query("DELETE FROM StreamPageDb WHERE name = :name")
    fun removeAllPagesByName(name: String)
}