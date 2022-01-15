package com.example.messengerapp.data.db.mapper.to_db

import com.example.messengerapp.data.db.entities.StreamDb
import com.example.messengerapp.data.db.pojo.StreamPojo
import com.example.messengerapp.domain.entity.channel.Stream
import javax.inject.Inject

interface StreamToStreamPojoMapper : (Stream, Boolean) -> StreamPojo

class StreamToStreamPojoMapperImpl @Inject constructor(
    private val topicToTopicDbMapper: TopicToTopicDbMapper,
    private val streamToStreamPage: StreamToStreamPageDbMapper
) : StreamToStreamPojoMapper {
    override fun invoke(stream: Stream, isSubscribed: Boolean): StreamPojo {
        val streamDb = StreamDb(
            streamId = stream.streamId,
            name = stream.name
        )
        return StreamPojo(
            streamDb = streamDb,
            streamPageDbs = listOf(streamToStreamPage(stream, isSubscribed)),
            topics = stream.topics.map { topic ->
                topicToTopicDbMapper(topic, stream.streamId)
            }
        )
    }

}