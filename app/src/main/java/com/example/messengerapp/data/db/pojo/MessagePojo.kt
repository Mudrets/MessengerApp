package com.example.messengerapp.data.db.pojo

import androidx.room.Embedded
import androidx.room.Relation
import com.example.messengerapp.data.db.entities.MessageDb
import com.example.messengerapp.data.db.entities.ReactionDb

data class MessagePojo(
    @Embedded
    val messageDb: MessageDb,
    @Relation(
        parentColumn = "messageId",
        entityColumn = "messageId"
    )
    val reactions: List<ReactionDb>
)
