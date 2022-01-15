package com.example.messengerapp.presentation.elm.stream

import com.example.messengerapp.di.annotation.qualifier.AllStreamsReducer
import com.example.messengerapp.di.annotation.qualifier.SubsStreamsReducer
import com.example.messengerapp.presentation.elm.stream.model.StreamState
import vivid.money.elmslie.core.store.ElmStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StreamStoreFactory @Inject constructor(
    private val actor: StreamActor,
    @SubsStreamsReducer private val subscribedStreamsReducer: StreamReducer,
    @AllStreamsReducer private val allStreamsReducer: StreamReducer
) {

    private val subsStore by lazy {
        ElmStore(
            initialState = StreamState.Loading,
            reducer = subscribedStreamsReducer,
            actor = actor
        )
    }

    private val allStore by lazy {
        ElmStore(
            initialState = StreamState.Loading,
            reducer = allStreamsReducer,
            actor = actor
        )
    }

    fun provide(isSubscribed: Boolean) = if (isSubscribed) subsStore else allStore
}