package com.example.messengerapp.data.network.mapper

import com.example.messengerapp.data.network.ZulipApi
import com.example.messengerapp.data.network.dto.SubscriptionsResponse
import com.example.messengerapp.domain.entity.channel.Stream
import com.example.messengerapp.domain.entity.channel.Topic
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class NetworkSubscriptionToStreamMapperImpl @Inject constructor(
    private val zulipApi: ZulipApi
) : NetworkSubscriptionToStreamMapper {

    override fun invoke(subscription: SubscriptionsResponse.Subscription): Single<Stream> {
        return zulipApi.getTopicsByStreamId(subscription.streamId).map { topicResponse ->
            val topicsList = topicResponse.topics.map { topic ->
                Topic(
                    name = topic.name,
                    maxId = topic.maxId
                )
            }
            Stream(
                name = subscription.name,
                topics = topicsList,
                streamId = subscription.streamId
            )
        }
    }

}