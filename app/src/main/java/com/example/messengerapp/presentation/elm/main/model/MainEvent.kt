package com.example.messengerapp.presentation.elm.main.model

import com.example.messengerapp.EmojiInfo
import com.example.messengerapp.domain.entity.user.User

sealed class MainEvent {
    sealed class Ui : MainEvent() {
        object GetUserInfo : Ui()
        object GetEmojiData : Ui()
    }

    sealed class Internal : MainEvent() {
        data class Error(val th: Throwable) : Internal()

        data class UserDataLoaded(val userInfo: User) : Internal()

        data class EmojiDataLoaded(val emojiInfo: EmojiInfo) : Internal()

        object Nothing : Internal()
    }
}