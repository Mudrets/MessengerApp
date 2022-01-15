package com.example.messengerapp.presentation.elm.profile

import com.example.messengerapp.presentation.elm.profile.model.ProfileCommand
import com.example.messengerapp.presentation.elm.profile.model.ProfileEffect
import com.example.messengerapp.presentation.elm.profile.model.ProfileEvent
import com.example.messengerapp.presentation.elm.profile.model.ProfileState
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject
import javax.inject.Provider

class ProfileReducer @Inject constructor(
    private val authorizedUserId: Provider<Long>
) :
    DslReducer<ProfileEvent, ProfileState, ProfileEffect, ProfileCommand>() {

    override fun Result.reduce(event: ProfileEvent): Any = when (event) {
        is ProfileEvent.Internal.Error -> {
            effects { +ProfileEffect.ShowError(event.th) }
        }
        is ProfileEvent.Internal.StatusLoaded -> {
            state { ProfileState.Success(event.status) }
        }
        is ProfileEvent.Ui.GetStatus -> {
            state { ProfileState.Loading }
            commands { +ProfileCommand.GetStatus(authorizedUserId.get()) }
        }
        ProfileEvent.Ui.Init -> {
        }
    }
}