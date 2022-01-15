package com.example.messengerapp.data.network.mapper

import com.example.messengerapp.data.network.dto.NetworkUser
import com.example.messengerapp.domain.entity.user.User
import com.example.messengerapp.domain.entity.user.UserStatus
import javax.inject.Inject

class NetworkUserToUserMapperImpl @Inject constructor() : NetworkUserToUserMapper {
    override fun invoke(networkUser: NetworkUser): User =
        User(
            fullName = networkUser.fullName,
            email = networkUser.email,
            avatarUrl = networkUser.avatarUrl,
            status = UserStatus.OFFLINE,
            userId = networkUser.userId
        )
}