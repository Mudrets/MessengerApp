package com.example.messengerapp.presentation.elm.people.model

sealed class PeopleCommand {
    data class SearchUsers(val searchQuery: String) : PeopleCommand()
    data class GetUserStatus(val userId: Long) : PeopleCommand()
}