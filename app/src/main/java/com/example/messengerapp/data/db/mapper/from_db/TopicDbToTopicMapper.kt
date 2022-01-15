package com.example.messengerapp.data.db.mapper.from_db

import com.example.messengerapp.data.db.entities.TopicDb
import com.example.messengerapp.domain.entity.channel.Topic
import javax.inject.Inject

interface TopicDbToTopicMapper : (TopicDb) -> Topic

class TopicDbToTopicMapperImpl @Inject constructor() : TopicDbToTopicMapper {

    override fun invoke(topicDb: TopicDb): Topic =
        Topic(
            name = topicDb.name,
            maxId = topicDb.maxId
        )

}