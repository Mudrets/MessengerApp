package com.example.messengerapp.presentation.elm.main.model

sealed class MainCommand {
    object GetUserInfo : MainCommand()
    object GetEmojiData : MainCommand()
}