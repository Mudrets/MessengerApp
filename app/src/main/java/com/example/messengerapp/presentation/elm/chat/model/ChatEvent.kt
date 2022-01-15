package com.example.messengerapp.presentation.elm.chat.model

import com.example.messengerapp.presentation.recyclerview.base.ViewTyped

sealed class ChatEvent {
    sealed class Ui : ChatEvent() {
        data class SendMessage(
            val messageText: String,
            val streamId: Long,
            val topicName: String
        ) : Ui()

        data class LoadFirstPage(
            val streamId: Long,
            val topicName: String
        ) : Ui()

        data class LoadNextPage(
            val streamId: Long,
            val topicName: String
        ) : Ui()

        data class AddReaction(
            val messageId: Long,
            val emojiName: String
        ) : Ui()

        data class RemoveReaction(
            val messageId: Long,
            val emojiName: String
        ) : Ui()

        data class DeleteMessage(val messageId: Long) : Ui()

        data class EditMessage(val messageId: Long, val content: String) : Ui()

        data class ChangeTopic(val messageId: Long, val topicName: String) : Ui()

        object Init : Ui()
    }

    sealed class Internal : ChatEvent() {
        data class ErrorLoading(val th: Throwable, val pageNum: Int) : Internal()

        data class PageLoaded(val messages: List<ViewTyped>, val numPage: Int) : Internal()

        data class ErrorMessageSent(val messageId: Long) : Internal()

        data class MessageActionError(val messageId: Long, val error: Throwable) : Internal()

        data class MessageSent(
            val sendingId: Long,
            val viewTypes: List<ViewTyped>,
            val realId: Long
        ) : Internal()

        data class AddReaction(val messageId: Long, val emojiName: String) : Internal()

        data class RemoveReaction(val messageId: Long, val emojiName: String) : Internal()

        data class MessageDeleted(val messageId: Long) : Internal()

        data class MessageEdited(val messageId: Long, val content: String) : Internal()

        data class TopicChanged(val messageId: Long, val topicName: String) : Internal()

        object Nothing : Internal()
    }
}