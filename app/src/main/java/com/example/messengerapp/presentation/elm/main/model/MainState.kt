package com.example.messengerapp.presentation.elm.main.model

import com.example.messengerapp.EmojiInfo
import com.example.messengerapp.domain.entity.user.User

sealed class MainState {
    object Loading : MainState()

    data class Error(val th: Throwable) : MainState()

    data class SuccessUserInfo(val user: User) : MainState()

    data class SuccessEmojiData(val emojiInfo: EmojiInfo) : MainState()
}