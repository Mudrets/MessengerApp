package com.example.messengerapp.presentation.elm.profile

import com.example.messengerapp.domain.usecase.people.GetUserStatusUseCase
import com.example.messengerapp.presentation.elm.profile.model.ProfileCommand
import com.example.messengerapp.presentation.elm.profile.model.ProfileEvent
import io.reactivex.rxjava3.core.Observable
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class ProfileActor @Inject constructor(
    private val getUserStatusUseCase: GetUserStatusUseCase
) : Actor<ProfileCommand, ProfileEvent> {

    override fun execute(command: ProfileCommand): Observable<ProfileEvent> = when (command) {
        is ProfileCommand.GetStatus -> {
            getUserStatusUseCase(command.userId)
                .mapEvents(
                    { (_, status) ->
                        ProfileEvent.Internal.StatusLoaded(status)
                    },
                    { error -> ProfileEvent.Internal.Error(error) }
                )
        }
    }
}