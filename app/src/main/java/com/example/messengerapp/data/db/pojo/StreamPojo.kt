package com.example.messengerapp.data.db.pojo

import androidx.room.Embedded
import androidx.room.Relation
import com.example.messengerapp.data.db.entities.StreamDb
import com.example.messengerapp.data.db.entities.StreamPageDb
import com.example.messengerapp.data.db.entities.TopicDb

data class StreamPojo(
    @Embedded
    val streamDb: StreamDb,
    @Relation(
        parentColumn = "stream_id",
        entityColumn = "stream_id"
    )
    val streamPageDbs: List<StreamPageDb>,
    @Relation(
        parentColumn = "stream_id",
        entityColumn = "stream_id"
    )
    val topics: List<TopicDb>
)
