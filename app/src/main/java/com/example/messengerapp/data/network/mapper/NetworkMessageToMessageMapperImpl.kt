package com.example.messengerapp.data.network.mapper

import com.example.messengerapp.data.network.dto.MessagesResponse
import com.example.messengerapp.domain.entity.chat.Emoji
import com.example.messengerapp.domain.entity.chat.Message
import com.example.messengerapp.domain.entity.chat.SendingStatus
import com.example.messengerapp.util.ext.convertStringToEmojiCode
import java.util.*
import javax.inject.Inject
import javax.inject.Provider
import kotlin.collections.HashMap

class NetworkMessageToMessageMapperImpl @Inject constructor(
    private val authorizedUserId: Provider<Long>
) : NetworkMessageToMessageMapper {

    override fun invoke(networkMessage: MessagesResponse.Message): Message {
        val nameHashMap = HashMap<String, MutableList<Long>>()
        networkMessage.reactions.forEach { reaction ->
            val emojiName = reaction.emojiName
            if (nameHashMap.containsKey(emojiName)) {
                nameHashMap[emojiName]?.add(reaction.userId)
            } else {
                nameHashMap[emojiName] = mutableListOf(reaction.userId)
            }
        }
        val emojiList = mutableListOf<Emoji>()
        nameHashMap.forEach { (emojiName, ids) ->
            val reaction = networkMessage.reactions.find {
                it.emojiName == emojiName
            }
            emojiList.add(
                Emoji(
                    emojiName = emojiName,
                    emojiCode = reaction?.emojiCode.convertStringToEmojiCode(),
                    userIds = ids
                )
            )
        }

        return Message(
            id = networkMessage.id,
            textMessage = networkMessage.content,
            senderFullName = networkMessage.senderFullName,
            avatarUrl = networkMessage.avatarUrl,
            date = Date(networkMessage.timestamp * 1000),
            emojiList = emojiList,
            sendingStatus = SendingStatus.SUCCESS,
            senderId = networkMessage.senderId,
            isMyMessage = networkMessage.senderId == authorizedUserId.get(),
            senderEmail = networkMessage.senderEmail,
            topicName = networkMessage.subject
        )
    }

}