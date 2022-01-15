package com.example.messengerapp.presentation.mapper

import com.example.messengerapp.domain.entity.user.User
import com.example.messengerapp.presentation.recyclerview.user.UserUi
import javax.inject.Inject

class UserToUserUiMapperImpl @Inject constructor() : UserToUserUiMapper {
    override fun invoke(user: User): UserUi =
        UserUi(
            name = user.fullName,
            email = user.email,
            status = user.status,
            avatarUrl = user.avatarUrl,
            uid = user.userId.toString()
        )
}