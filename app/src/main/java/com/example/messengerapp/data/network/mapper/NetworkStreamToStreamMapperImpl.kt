package com.example.messengerapp.data.network.mapper

import com.example.messengerapp.data.network.ZulipApi
import com.example.messengerapp.data.network.dto.StreamResponse
import com.example.messengerapp.domain.entity.channel.Stream
import com.example.messengerapp.domain.entity.channel.Topic
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class NetworkStreamToStreamMapperImpl @Inject constructor(
    private val zulipApi: ZulipApi
) : NetworkStreamToStreamMapper {

    override fun invoke(networkStream: StreamResponse.Stream): Single<Stream> =
        zulipApi.getTopicsByStreamId(networkStream.streamId).map { topicResponse ->
            val topicsList = topicResponse.topics.map { topic ->
                Topic(
                    name = topic.name,
                    maxId = topic.maxId
                )
            }
            Stream(
                name = networkStream.name,
                topics = topicsList,
                streamId = networkStream.streamId
            )
        }

}