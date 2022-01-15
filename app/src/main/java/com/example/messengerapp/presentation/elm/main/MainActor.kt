package com.example.messengerapp.presentation.elm.main

import com.example.messengerapp.AuthorizedUser
import com.example.messengerapp.EmojiInfo
import com.example.messengerapp.presentation.elm.main.model.MainCommand
import com.example.messengerapp.presentation.elm.main.model.MainEvent
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class MainActor @Inject constructor(
    private val authorizedUser: AuthorizedUser,
    private val emojiInfo: EmojiInfo
) : Actor<MainCommand, MainEvent> {
    override fun execute(command: MainCommand): Observable<MainEvent> = when (command) {
        MainCommand.GetUserInfo -> {
            authorizedUser.getUser()
                .mapEvents(
                    { user ->
                        if (!AuthorizedUser.isErrorUser(user)) {
                            Timber.e(user.toString())
                            MainEvent.Internal.UserDataLoaded(user)
                        } else {
                            MainEvent.Internal.Nothing
                        }
                    },
                    { error ->
                        Timber.e(error)
                        MainEvent.Internal.Error(Throwable("userData"))
                    }
                )
        }
        MainCommand.GetEmojiData -> {
            emojiInfo.getEmojiData()
                .mapEvents(
                    { response ->
                        Timber.e(response.toString())
                        MainEvent.Internal.EmojiDataLoaded(emojiInfo)
                    },
                    { error ->
                        Timber.e(error)
                        MainEvent.Internal.Error(Throwable("emojiData"))
                    }
                )
        }
    }
}