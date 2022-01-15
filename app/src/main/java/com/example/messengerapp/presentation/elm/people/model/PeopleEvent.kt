package com.example.messengerapp.presentation.elm.people.model

import com.example.messengerapp.domain.entity.user.User
import com.example.messengerapp.domain.entity.user.UserStatus
import com.example.messengerapp.presentation.recyclerview.user.UserUi

sealed class PeopleEvent {
    sealed class Ui : PeopleEvent() {
        object GetUsers : Ui()

        data class ClickOnUser(val user: User) : Ui()

        data class SearchUsers(val searchQuery: String) : Ui()

        data class GetUserStatus(val userId: Long) : Ui()
    }

    sealed class Internal : PeopleEvent() {
        data class ErrorSearching(val th: Throwable) : Internal()

        data class UsersLoaded(val users: List<UserUi>) : Internal()

        data class UserStatusLoaded(val userId: Long, val userStatus: UserStatus) : Internal()

        object ShowLoad : Internal()

        object Nothing : Internal()
    }
}