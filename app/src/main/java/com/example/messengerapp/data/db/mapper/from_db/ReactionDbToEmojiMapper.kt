package com.example.messengerapp.data.db.mapper.from_db

import com.example.messengerapp.data.db.entities.ReactionDb
import com.example.messengerapp.domain.entity.chat.Emoji
import javax.inject.Inject

interface ReactionListDbToEmojiListMapper : (List<ReactionDb>) -> List<Emoji>

class ReactionListDbToEmojiListMapperImpl @Inject constructor() : ReactionListDbToEmojiListMapper {

    override fun invoke(reactions: List<ReactionDb>): List<Emoji> =
        reactions.groupBy(ReactionDb::emojiName).map { (name, reactions) ->
            Emoji(
                emojiName = name,
                emojiCode = reactions[0].emojiCode,
                userIds = reactions.map(ReactionDb::userId)
            )
        }
}