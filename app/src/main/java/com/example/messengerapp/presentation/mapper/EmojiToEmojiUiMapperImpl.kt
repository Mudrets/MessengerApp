package com.example.messengerapp.presentation.mapper

import com.example.messengerapp.domain.entity.chat.Emoji
import com.example.messengerapp.presentation.view_group.message.EmojiUi
import javax.inject.Inject
import javax.inject.Provider

class EmojiToEmojiUiMapperImpl @Inject constructor(
    private val authorizedUserId: Provider<Long>
) : EmojiToEmojiUiMapper {
    override fun invoke(emoji: Emoji): EmojiUi =
        EmojiUi(
            emojiCode = emoji.emojiCode,
            emojiName = emoji.emojiName,
            count = emoji.count,
            isSelected = emoji.userIds.contains(authorizedUserId.get())
        )

}