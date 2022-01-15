package com.example.messengerapp.presentation.elm.main

import com.example.messengerapp.presentation.elm.main.model.MainCommand
import com.example.messengerapp.presentation.elm.main.model.MainEffect
import com.example.messengerapp.presentation.elm.main.model.MainEvent
import com.example.messengerapp.presentation.elm.main.model.MainState
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

class MainReducer @Inject constructor() :
    DslReducer<MainEvent, MainState, MainEffect, MainCommand>() {
    override fun Result.reduce(event: MainEvent): Any = when (event) {
        is MainEvent.Internal.Error -> {
            if (event.th.message == "userData") {
                state { MainState.Error(event.th) }
                commands { +MainCommand.GetUserInfo }
            } else {
                commands { +MainCommand.GetEmojiData }
            }
        }
        is MainEvent.Internal.UserDataLoaded -> {
            state { MainState.SuccessUserInfo(event.userInfo) }
        }
        MainEvent.Ui.GetUserInfo -> {
            state { MainState.Loading }
            commands { +MainCommand.GetUserInfo }
        }
        MainEvent.Ui.GetEmojiData -> {
            commands { +MainCommand.GetEmojiData }
        }
        is MainEvent.Internal.EmojiDataLoaded -> {
            state { MainState.SuccessEmojiData(event.emojiInfo) }
        }
        MainEvent.Internal.Nothing -> {
        }
    }
}