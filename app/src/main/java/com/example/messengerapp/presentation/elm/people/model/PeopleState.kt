package com.example.messengerapp.presentation.elm.people.model

import com.example.messengerapp.presentation.recyclerview.user.UserUi

sealed class PeopleState {
    object Loading : PeopleState()

    data class Error(val th: Throwable) : PeopleState()

    data class Success(val users: List<UserUi>) : PeopleState()
}