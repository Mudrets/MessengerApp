package com.example.messengerapp.presentation.mapper

import com.example.messengerapp.R
import com.example.messengerapp.domain.entity.channel.Stream
import com.example.messengerapp.presentation.recyclerview.stream.StreamUi
import javax.inject.Inject

class StreamToStreamUiMapperImpl @Inject constructor(
    private val topicToTopicUiMapper: TopicToTopicUiMapper
) : StreamToStreamUiMapper {
    override fun invoke(stream: Stream): StreamUi {
        val topics = stream.topics.mapIndexed { index, topic ->
            topicToTopicUiMapper(index, topic, stream.name, stream.streamId)
        }.sortedWith { topic1, topic2 ->
            topic2.streamName.compareTo(topic1.streamName, ignoreCase = true)
        }
        return StreamUi(
            streamName = stream.name,
            items = topics,
            uid = stream.name,
            activeArrowColorRes = R.color.text_color,
            notActiveArrowColorRes = R.color.expand_arrow_color,
            titleTmpRes = R.string.stream_title,
            streamId = stream.streamId
        )
    }
}
