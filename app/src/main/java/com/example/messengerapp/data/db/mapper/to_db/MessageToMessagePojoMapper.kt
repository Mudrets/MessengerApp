package com.example.messengerapp.data.db.mapper.to_db

import com.example.messengerapp.data.db.entities.MessageDb
import com.example.messengerapp.data.db.pojo.MessagePojo
import com.example.messengerapp.domain.entity.chat.Message
import javax.inject.Inject

interface MessageToMessagePojoMapper : (Message, String, Long) -> MessagePojo

class MessageToMessagePojoMapperImpl @Inject constructor(
    private val emojiListToReactionDbListMapper: EmojiListToReactionDbListMapper
) : MessageToMessagePojoMapper {
    override fun invoke(message: Message, topic: String, streamId: Long): MessagePojo {
        val messageDb = MessageDb(
            messageId = message.id,
            topicName = if (topic.isBlank()) message.topicName else topic,
            streamId = streamId,
            senderFullName = message.senderFullName,
            senderId = message.senderId,
            senderEmail = message.senderEmail,
            avatarUrl = message.avatarUrl,
            content = message.textMessage,
            timestampSeconds = message.date.time,
            fromMe = message.isMyMessage
        )
        return MessagePojo(
            messageDb = messageDb,
            reactions = emojiListToReactionDbListMapper(message.emojiList, message.id)
        )
    }

}