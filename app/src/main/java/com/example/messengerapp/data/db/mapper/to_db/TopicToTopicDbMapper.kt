package com.example.messengerapp.data.db.mapper.to_db

import com.example.messengerapp.data.db.entities.TopicDb
import com.example.messengerapp.domain.entity.channel.Topic
import javax.inject.Inject

interface TopicToTopicDbMapper : (Topic, Long) -> TopicDb

class TopicToTopicDbMapperImpl @Inject constructor() : TopicToTopicDbMapper {
    override fun invoke(topic: Topic, streamId: Long): TopicDb =
        TopicDb(
            id = 0,
            name = topic.name,
            streamId = streamId,
            maxId = topic.maxId
        )

}