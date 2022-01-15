package com.example.messengerapp.presentation.elm.people

import com.example.messengerapp.domain.entity.user.UserStatus
import com.example.messengerapp.presentation.elm.people.model.PeopleCommand
import com.example.messengerapp.presentation.elm.people.model.PeopleEffect
import com.example.messengerapp.presentation.elm.people.model.PeopleEvent
import com.example.messengerapp.presentation.elm.people.model.PeopleState
import com.example.messengerapp.presentation.recyclerview.user.UserUi
import com.example.messengerapp.util.Constants.INITIAL_QUERY
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import java.util.*
import javax.inject.Inject

class PeopleReducer @Inject constructor() :
    DslReducer<PeopleEvent, PeopleState, PeopleEffect, PeopleCommand>() {
    private val userMap = mutableMapOf<Long, Pair<UserStatus, Long>>()

    override fun Result.reduce(event: PeopleEvent): Any = when (event) {
        is PeopleEvent.Internal.ErrorSearching -> {
            state { PeopleState.Error(event.th) }
        }
        is PeopleEvent.Internal.UsersLoaded -> {
            state { PeopleState.Success(event.users) }
        }
        is PeopleEvent.Ui.ClickOnUser -> {
            effects { +PeopleEffect.OpenUserDetails(event.user) }
        }
        PeopleEvent.Ui.GetUsers -> {
            state { PeopleState.Loading }
            commands { +PeopleCommand.SearchUsers(INITIAL_QUERY) }
        }
        is PeopleEvent.Ui.SearchUsers -> {
            if ((state as? PeopleState.Success)?.users.isNullOrEmpty())
                state { PeopleState.Loading }
            commands { +PeopleCommand.SearchUsers(event.searchQuery) }
        }
        is PeopleEvent.Internal.UserStatusLoaded -> {
            if (event.userStatus == UserStatus.OFFLINE) {
                userMap.remove(event.userId)
                Any()
            } else {
                userMap[event.userId] = event.userStatus to now
                val users = getUsers(state)
                if (users != null) {
                    state {
                        PeopleState.Success(users.changeStatus(event.userId, event.userStatus))
                    }
                } else {
                    Any()
                }
            }
        }
        is PeopleEvent.Ui.GetUserStatus -> {
            val users = getUsers(state)
            if (hasActualStatus(event.userId) && users != null) {
                state {
                    PeopleState.Success(
                        users.changeStatus(
                            event.userId,
                            userMap[event.userId]?.first!!
                        )
                    )
                }
            } else {
                commands { +PeopleCommand.GetUserStatus(event.userId) }
            }
        }
        PeopleEvent.Internal.Nothing -> {
        }
        PeopleEvent.Internal.ShowLoad -> {
            state { PeopleState.Loading }
        }
    }

    private fun List<UserUi>.changeStatus(userId: Long, userStatus: UserStatus): List<UserUi> {
        val mutableList = mutableListOf<UserUi>()
        mutableList.addAll(this)
        val userIndex = mutableList.indexOfFirst { user -> user.uid.toLong() == userId }
        if (userIndex != -1)
            mutableList[userIndex] = get(userIndex).copy(status = userStatus)
        return mutableList
    }

    private fun hasActualStatus(userId: Long): Boolean =
        userMap.containsKey(userId) && userMap[userId]?.second.isActualTime()

    private fun getUsers(state: PeopleState): List<UserUi>? =
        (state as? PeopleState.Success)?.users

    private fun Long?.isActualTime() = this != null && now - this <= ACTUAL_TIME

    private val now: Long
        get() = Date().time / 1000

    companion object {
        private const val ACTUAL_TIME = 60
    }
}