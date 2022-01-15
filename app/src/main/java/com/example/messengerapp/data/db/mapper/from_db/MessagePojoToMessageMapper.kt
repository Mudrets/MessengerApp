package com.example.messengerapp.data.db.mapper.from_db

import com.example.messengerapp.data.db.pojo.MessagePojo
import com.example.messengerapp.domain.entity.chat.Message
import com.example.messengerapp.domain.entity.chat.SendingStatus
import java.util.*
import javax.inject.Inject

interface MessagePojoToMessageMapper : (MessagePojo) -> Message

class MessagePojoToMessageMapperImpl @Inject constructor(
    private val reactionDbToEmojiMapper: ReactionListDbToEmojiListMapper
) : MessagePojoToMessageMapper {

    override fun invoke(messagePojo: MessagePojo): Message =
        Message(
            id = messagePojo.messageDb.messageId,
            textMessage = messagePojo.messageDb.content,
            senderFullName = messagePojo.messageDb.senderFullName,
            avatarUrl = messagePojo.messageDb.avatarUrl,
            date = Date(messagePojo.messageDb.timestampSeconds),
            emojiList = reactionDbToEmojiMapper(messagePojo.reactions),
            sendingStatus = SendingStatus.SUCCESS,
            senderId = messagePojo.messageDb.senderId,
            isMyMessage = messagePojo.messageDb.fromMe,
            senderEmail = messagePojo.messageDb.senderEmail,
            topicName = messagePojo.messageDb.topicName
        )

}