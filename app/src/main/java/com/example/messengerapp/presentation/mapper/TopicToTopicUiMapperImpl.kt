package com.example.messengerapp.presentation.mapper

import com.example.messengerapp.domain.entity.channel.Topic
import com.example.messengerapp.presentation.recyclerview.stream.TopicUi
import com.example.messengerapp.util.Constants.COLORS
import javax.inject.Inject

class TopicToTopicUiMapperImpl @Inject constructor() : TopicToTopicUiMapper {
    override fun invoke(index: Int, topic: Topic, streamName: String, streamId: Long): TopicUi =
        TopicUi(
            topicName = topic.name,
            streamName = streamName,
            color = COLORS[index % COLORS.size],
            streamId = streamId
        )

}