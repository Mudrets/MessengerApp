package com.example.messengerapp.data.db.mapper.from_db

import com.example.messengerapp.data.db.pojo.StreamPojo
import com.example.messengerapp.domain.entity.channel.Stream
import javax.inject.Inject

interface StreamPojoToStreamMapper : (StreamPojo) -> Stream

class StreamPojoToStreamMapperImpl @Inject constructor(
    private val topicDbToTopicMapper: TopicDbToTopicMapper
) : StreamPojoToStreamMapper {

    override fun invoke(streamPojo: StreamPojo): Stream =
        Stream(
            name = streamPojo.streamDb.name,
            streamId = streamPojo.streamDb.streamId,
            topics = streamPojo.topics.map(topicDbToTopicMapper)
        )

}