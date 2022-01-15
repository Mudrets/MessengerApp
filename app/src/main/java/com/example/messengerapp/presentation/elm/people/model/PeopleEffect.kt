package com.example.messengerapp.presentation.elm.people.model

import com.example.messengerapp.domain.entity.user.User

sealed class PeopleEffect {
    data class OpenUserDetails(
        val user: User
    ) : PeopleEffect()
}