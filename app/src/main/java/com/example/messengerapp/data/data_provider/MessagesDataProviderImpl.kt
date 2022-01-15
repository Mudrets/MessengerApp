package com.example.messengerapp.data.data_provider

import com.example.messengerapp.data.network.ZulipApi
import com.example.messengerapp.data.network.dto.Narrow
import com.example.messengerapp.data.network.mapper.NetworkMessageToMessageMapper
import com.example.messengerapp.domain.entity.chat.Message
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class MessagesDataProviderImpl @Inject constructor(
    private val zulipApi: ZulipApi,
    private val networkMessageToMessage: NetworkMessageToMessageMapper
) : MessagesDataProvider {

    private val moshi = Moshi.Builder().build()

    private fun createNarrow(streamId: Long, topic: String = ""): String {
        val type = Types.newParameterizedType(
            List::class.java,
            Narrow::class.java,
        )
        val adapter: JsonAdapter<List<Narrow>> = moshi.adapter(type)
        val narrowParams = if (topic.isBlank()) {
            listOf(
                Narrow(operator = "stream", operand = streamId)
            )
        } else {
            listOf(
                Narrow(operator = "topic", operand = topic),
                Narrow(operator = "stream", operand = streamId)
            )
        }
        Timber.d(adapter.toJson(narrowParams))
        return adapter.toJson(narrowParams)
    }

    override fun getMessages(
        anchor: Long,
        numBefore: Long,
        numAfter: Long,
        topic: String,
        streamId: Long
    ): Single<List<Message>> =
        zulipApi.getMessages(
            anchor = anchor,
            numBefore = numBefore,
            numAfter = numAfter,
            narrow = createNarrow(streamId, topic)
        )
            .subscribeOn(Schedulers.io())
            .toObservable()
            .flatMapIterable { response -> response.messages }
            .map { networkMessage ->
                networkMessageToMessage(networkMessage)
            }
            .toList()
}