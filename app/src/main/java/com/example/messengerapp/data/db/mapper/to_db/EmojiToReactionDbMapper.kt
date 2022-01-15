package com.example.messengerapp.data.db.mapper.to_db

import com.example.messengerapp.data.db.entities.ReactionDb
import com.example.messengerapp.domain.entity.chat.Emoji
import javax.inject.Inject

interface EmojiListToReactionDbListMapper : (List<Emoji>, Long) -> List<ReactionDb>

class EmojiListToReactionDbListMapperImpl @Inject constructor() : EmojiListToReactionDbListMapper {
    override fun invoke(emojiList: List<Emoji>, messageId: Long): List<ReactionDb> =
        emojiList.flatMap { emoji ->
            emoji.userIds.map { userId ->
                ReactionDb(
                    uid = 0,
                    messageId = messageId,
                    userId = userId,
                    emojiName = emoji.emojiName,
                    emojiCode = emoji.emojiCode
                )
            }
        }

}