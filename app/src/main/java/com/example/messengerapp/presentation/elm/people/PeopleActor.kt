package com.example.messengerapp.presentation.elm.people

import com.example.messengerapp.domain.usecase.people.GetUserStatusUseCase
import com.example.messengerapp.domain.usecase.people.SearchPeopleUseCase
import com.example.messengerapp.presentation.elm.people.model.PeopleCommand
import com.example.messengerapp.presentation.elm.people.model.PeopleEvent
import com.example.messengerapp.presentation.mapper.UserToUserUiMapper
import com.example.messengerapp.util.Constants
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class PeopleActor @Inject constructor(
    private val searchPeopleUseCase: SearchPeopleUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val userToUserUiMapper: UserToUserUiMapper
) : Actor<PeopleCommand, PeopleEvent> {

    private var currQuery = Constants.INITIAL_QUERY

    override fun execute(command: PeopleCommand): Observable<PeopleEvent> = when (command) {
        is PeopleCommand.SearchUsers -> {
            var responseNum = 0
            currQuery = command.searchQuery
            searchPeopleUseCase(command.searchQuery)
                .map { people ->
                    people.map(userToUserUiMapper).sortedWith { user1, user2 ->
                        user1.name.compareTo(user2.name, ignoreCase = true)
                    }
                }
                .mapEvents(
                    { users ->
                        Timber.e("$currQuery ${responseNum++}")
                        if (currQuery == command.searchQuery) {
                            if (responseNum == 1 && users.isEmpty()) {
                                PeopleEvent.Internal.ShowLoad
                            } else {
                                PeopleEvent.Internal.UsersLoaded(users)
                            }
                        } else {
                            PeopleEvent.Internal.Nothing
                        }
                    },
                    { error -> PeopleEvent.Internal.ErrorSearching(error) }
                )
        }
        is PeopleCommand.GetUserStatus -> {
            getUserStatusUseCase(command.userId)
                .mapSuccessEvent { (userId, userStatus) ->
                    PeopleEvent.Internal.UserStatusLoaded(userId, userStatus)
                }
        }
    }
}