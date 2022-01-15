package com.example.messengerapp.presentation.elm.chat.model

sealed class ChatCommand {
    data class SendMessage(
        val messageText: String,
        val streamId: Long,
        val topicName: String,
        val sendingId: Long
    ) : ChatCommand()

    data class AddReaction(val messageId: Long, val emojiName: String) : ChatCommand()

    data class RemoveReaction(val messageId: Long, val emojiName: String) : ChatCommand()

    data class LoadPage(
        val streamId: Long,
        val topicName: String,
        val pageNum: Int
    ) : ChatCommand()

    data class DeleteMessage(val messageId: Long) : ChatCommand()

    data class EditMessage(val messageId: Long, val content: String) : ChatCommand()

    data class ChangeTopic(val messageId: Long, val topicName: String) : ChatCommand()
}