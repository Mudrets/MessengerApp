package com.example.messengerapp.presentation.mapper

import com.example.messengerapp.EmojiInfo
import com.example.messengerapp.domain.entity.chat.Message
import com.example.messengerapp.domain.entity.chat.MessageType
import com.example.messengerapp.presentation.recyclerview.chat.MessageUi
import com.example.messengerapp.util.Constants
import javax.inject.Inject

class MessageToMessageUiMapperImpl @Inject constructor(
    private val emojiToEmojiUiMapper: EmojiToEmojiUiMapper,
    private val emojiInfo: EmojiInfo
) : MessageToMessageUiMapper {
    override fun invoke(message: Message): MessageUi {
        val emojiList =
            message.emojiList.filter { !Constants.IGNORED_EMOJI_LIST.contains(it.emojiName) }
        val splitedText = message.textMessage.split(':')
        var messageText = ""
        var prevWasEmoji = false
        for ((index, text) in splitedText.withIndex()) {
            if (emojiInfo.getNames().contains(text)) {
                messageText += emojiInfo.getEmojiCode(text)
                prevWasEmoji = true
            } else {
                if (!prevWasEmoji && index != 0)
                    messageText += ":"
                messageText += text
                prevWasEmoji = false
            }
        }
        return MessageUi(
            messageText = messageText,
            senderName = message.senderFullName,
            messageType = message.type,
            avatarUrl = message.avatarUrl,
            emojiList = emojiList.map(emojiToEmojiUiMapper),
            messageId = message.id,
            sendingStatus = message.sendingStatus,
            topicName = message.topicName
        )
    }

    private val Message.type: MessageType
        get() = if (isMyMessage)
            MessageType.OUTGOING_MESSAGE
        else
            MessageType.INCOMING_MESSAGE
}