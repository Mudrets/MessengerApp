package com.example.messengerapp.presentation.elm.chat

import com.example.messengerapp.di.annotation.qualifier.StreamChatActor
import com.example.messengerapp.di.annotation.qualifier.StreamChatReducer
import com.example.messengerapp.presentation.elm.chat.model.ChatCommand
import com.example.messengerapp.presentation.elm.chat.model.ChatEffect
import com.example.messengerapp.presentation.elm.chat.model.ChatEvent
import com.example.messengerapp.presentation.elm.chat.model.ChatState
import vivid.money.elmslie.core.store.ElmStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatStoreFactory @Inject constructor(
    private val topicChatActor: ChatActor,
    @StreamChatActor
    private val streamChatActor: ChatActor,
    private val topicChatReducer: ChatReducer,
    @StreamChatReducer
    private val streamChatReducer: ChatReducer
) {
    private val stores =
        mutableMapOf<String, ElmStore<ChatEvent, ChatState, ChatEffect, ChatCommand>>()

    private fun createNewStore(
        key: String,
        isTopicChat: Boolean
    ): ElmStore<ChatEvent, ChatState, ChatEffect, ChatCommand> {
        stores[key] = ElmStore(
            actor = if (isTopicChat) topicChatActor else streamChatActor,
            reducer = if (isTopicChat) topicChatReducer else streamChatReducer,
            initialState = ChatState()
        )
        return stores[key]!!
    }

    fun provide(
        streamId: Long,
        topicName: String
    ): ElmStore<ChatEvent, ChatState, ChatEffect, ChatCommand> {
        val key = "$streamId.$topicName"
        return if (stores.containsKey(key))
            stores[key]!!
        else
            createNewStore(key, topicName.isNotBlank())
    }

}