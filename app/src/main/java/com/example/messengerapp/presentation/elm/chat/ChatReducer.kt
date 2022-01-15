package com.example.messengerapp.presentation.elm.chat

import com.example.messengerapp.domain.entity.chat.SendingStatus
import com.example.messengerapp.presentation.elm.chat.model.ChatCommand
import com.example.messengerapp.presentation.elm.chat.model.ChatEffect
import com.example.messengerapp.presentation.elm.chat.model.ChatEvent
import com.example.messengerapp.presentation.elm.chat.model.ChatState
import com.example.messengerapp.presentation.recyclerview.chat.pagination.ChatListManager
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

class ChatReducer @Inject constructor(
    private val chatListManager: ChatListManager
) : DslReducer<ChatEvent, ChatState, ChatEffect, ChatCommand>() {

    override fun Result.reduce(event: ChatEvent): Any = when (event) {
        is ChatEvent.Internal.MessageActionError -> messageActionError(event)
        is ChatEvent.Internal.ErrorLoading -> errorLoading(event)
        is ChatEvent.Internal.ErrorMessageSent -> errorSendingMessage(event)
        is ChatEvent.Internal.MessageSent -> messageSent(event)
        is ChatEvent.Internal.PageLoaded -> pageLoaded(event)
        is ChatEvent.Internal.AddReaction -> addReaction(event)
        is ChatEvent.Internal.RemoveReaction -> removeReaction(event)
        is ChatEvent.Internal.MessageDeleted -> messageDeleted(event)
        is ChatEvent.Internal.MessageEdited -> messageEdited(event)
        is ChatEvent.Internal.TopicChanged -> topicChanged(event)
        is ChatEvent.Ui.AddReaction -> uiAddReaction(event)
        is ChatEvent.Ui.RemoveReaction -> uiRemoveReaction(event)
        is ChatEvent.Ui.LoadFirstPage -> loadFirstPage(event)
        is ChatEvent.Ui.LoadNextPage -> loadNextPage(event)
        is ChatEvent.Ui.SendMessage -> sendMessage(event)
        is ChatEvent.Ui.DeleteMessage -> deleteMessage(event)
        is ChatEvent.Ui.ChangeTopic -> changeTopic(event)
        is ChatEvent.Ui.EditMessage -> editMessage(event)
        else -> {
        }
    }

    private fun Result.editMessage(event: ChatEvent.Ui.EditMessage) {
        chatListManager.setSendingStatus(event.messageId, SendingStatus.IS_SENDING)
        state { copy(messages = chatListManager.items) }
        commands { +ChatCommand.EditMessage(event.messageId, event.content) }
    }

    private fun Result.changeTopic(event: ChatEvent.Ui.ChangeTopic) {
        chatListManager.setSendingStatus(event.messageId, SendingStatus.IS_SENDING)
        state { copy(messages = chatListManager.items) }
        commands { +ChatCommand.ChangeTopic(event.messageId, event.topicName) }
    }

    private fun Result.topicChanged(event: ChatEvent.Internal.TopicChanged) {
        chatListManager.changeMessageTopic(event.messageId, SendingStatus.SUCCESS, event.topicName)
        state { copy(messages = chatListManager.items) }
    }

    private fun Result.messageEdited(event: ChatEvent.Internal.MessageEdited) {
        chatListManager.changeMessageContent(event.messageId, SendingStatus.SUCCESS, event.content)
        state { copy(messages = chatListManager.items) }
    }

    private fun Result.deleteMessage(event: ChatEvent.Ui.DeleteMessage) {
        chatListManager.setSendingStatus(event.messageId, SendingStatus.IS_SENDING)
        state { copy(messages = chatListManager.items) }
        commands { +ChatCommand.DeleteMessage(event.messageId) }
    }

    private fun Result.messageDeleted(event: ChatEvent.Internal.MessageDeleted) {
        chatListManager.deleteMessage(event.messageId)
        state {
            copy(
                messages = chatListManager.items,
                isEmptyState = chatListManager.items.isEmpty()
            )
        }
    }

    private fun Result.messageActionError(event: ChatEvent.Internal.MessageActionError) {
        chatListManager.setSendingStatus(event.messageId, SendingStatus.SUCCESS)
        state { copy(messages = chatListManager.items) }
        effects { +ChatEffect.ErrorEffect(event.error) }
    }

    private fun Result.errorLoading(event: ChatEvent.Internal.ErrorLoading) {
        if (event.pageNum == 0 && state.isLoading) {
            state { copy(error = event.th, isLoading = false) }
        } else {
            chatListManager.removeLoading()
        }
    }

    private fun Result.errorSendingMessage(event: ChatEvent.Internal.ErrorMessageSent) {
        chatListManager.setSendingStatus(event.messageId, SendingStatus.ERROR)
        state { copy(messages = chatListManager.items) }
    }

    private fun Result.messageSent(event: ChatEvent.Internal.MessageSent) {
        chatListManager.setSendingStatus(event.sendingId, SendingStatus.SUCCESS, event.realId)
        state { copy(messages = chatListManager.items) }
    }

    private fun Result.pageLoaded(event: ChatEvent.Internal.PageLoaded) {
        chatListManager.setPage(event.numPage, event.messages)
        chatListManager.removeLoading()
        state {
            copy(
                messages = chatListManager.items,
                error = null,
                isLoading = false,
                isEmptyState = chatListManager.items.isEmpty()
            )
        }
    }

    private fun Result.addReaction(event: ChatEvent.Internal.AddReaction) {
        chatListManager.addEmojiToMessage(event.messageId, event.emojiName, SendingStatus.SUCCESS)
        state { copy(messages = chatListManager.items) }
    }

    private fun Result.removeReaction(event: ChatEvent.Internal.RemoveReaction) {
        chatListManager.removeEmojiFromMessage(
            event.messageId,
            event.emojiName,
            SendingStatus.SUCCESS
        )
        state { copy(messages = chatListManager.items) }
    }

    private fun Result.uiAddReaction(event: ChatEvent.Ui.AddReaction) {
        chatListManager.setSendingStatus(event.messageId, SendingStatus.IS_SENDING)
        state { copy(messages = chatListManager.items) }
        commands { +ChatCommand.AddReaction(event.messageId, event.emojiName) }
    }

    private fun Result.uiRemoveReaction(event: ChatEvent.Ui.RemoveReaction) {
        chatListManager.setSendingStatus(event.messageId, SendingStatus.IS_SENDING)
        state { copy(messages = chatListManager.items) }
        commands { +ChatCommand.RemoveReaction(event.messageId, event.emojiName) }
    }

    private fun Result.loadFirstPage(event: ChatEvent.Ui.LoadFirstPage) {
        chatListManager.clear()
        state {
            copy(
                isLoading = true,
                isEmptyState = false,
                error = null,
                messages = chatListManager.items,
                pageNumber = 0
            )
        }
        commands {
            +ChatCommand.LoadPage(
                event.streamId,
                event.topicName,
                ChatState.INITIAL_PAGE
            )
        }
    }

    private fun Result.loadNextPage(event: ChatEvent.Ui.LoadNextPage) {
        if (!chatListManager.isLoading) {
            chatListManager.showLoading()
            val pageNumber = state.pageNumber + 1
            state { copy(messages = chatListManager.items, pageNumber = pageNumber) }
            commands { +ChatCommand.LoadPage(event.streamId, event.topicName, pageNumber) }
        } else {
            Any()
        }
    }

    private fun Result.sendMessage(event: ChatEvent.Ui.SendMessage) {
        val messageId = chatListManager.sendMessage(event.messageText, event.topicName)
        state { copy(messages = chatListManager.items, isEmptyState = false) }
        commands {
            +ChatCommand.SendMessage(event.messageText, event.streamId, event.topicName, messageId)
        }
    }
}