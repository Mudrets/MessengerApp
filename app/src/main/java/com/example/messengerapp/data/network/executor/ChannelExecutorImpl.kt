package com.example.messengerapp.data.network.executor

import com.example.messengerapp.data.network.ZulipApi
import com.example.messengerapp.data.network.dto.Subscription
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ChannelExecutorImpl @Inject constructor(
    private val zulipApi: ZulipApi
) : ChannelExecutor {

    private val moshi = Moshi.Builder().build()

    private fun createSubscriptions(streamName: String, description: String): String {
        val type = Types.newParameterizedType(
            List::class.java,
            Subscription::class.java,
        )
        val adapter: JsonAdapter<List<Subscription>> = moshi.adapter(type)
        val subscriptions = listOf(
            Subscription(name = streamName, description = description)
        )
        Timber.d(adapter.toJson(subscriptions))
        return adapter.toJson(subscriptions)
    }

    override fun createNewStream(streamName: String, description: String): Single<Boolean> {
        val subs = createSubscriptions(streamName, description)
        return zulipApi.subscribeToStream(subs)
            .subscribeOn(Schedulers.io())
            .map { response ->
                when {
                    response.result == "error" -> throw IllegalArgumentException("")
                    response.alreadySubscribed.isNotEmpty() -> throw IllegalArgumentException("Channel already exist")
                    else -> true
                }
            }
    }
}