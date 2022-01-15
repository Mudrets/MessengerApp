package com.example.messengerapp.presentation.mapper

import com.example.messengerapp.domain.entity.chat.Emoji
import com.example.messengerapp.presentation.view_group.message.EmojiUi

interface EmojiToEmojiUiMapper : (Emoji) -> EmojiUi