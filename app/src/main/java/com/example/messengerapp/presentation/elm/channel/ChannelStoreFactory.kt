package com.example.messengerapp.presentation.elm.channel

import com.example.messengerapp.presentation.elm.channel.model.ChannelState
import com.example.messengerapp.util.ext.fastLazy
import vivid.money.elmslie.core.store.ElmStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelStoreFactory @Inject constructor(
    private val reducer: ChannelReducer,
    private val actor: ChannelActor
) {

    private val store by fastLazy {
        ElmStore(
            reducer = reducer,
            actor = actor,
            initialState = ChannelState.Normal
        )
    }

    fun provide() = store
}