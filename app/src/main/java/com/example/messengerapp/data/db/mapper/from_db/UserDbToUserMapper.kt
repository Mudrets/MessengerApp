package com.example.messengerapp.data.db.mapper.from_db

import com.example.messengerapp.data.db.entities.UserDb
import com.example.messengerapp.domain.entity.user.User
import com.example.messengerapp.domain.entity.user.UserStatus
import javax.inject.Inject

interface UserDbToUserMapper : (UserDb) -> User

class UserDbToUserMapperImpl @Inject constructor() : UserDbToUserMapper {
    override fun invoke(userDb: UserDb): User =
        User(
            fullName = userDb.fullName,
            email = userDb.email,
            avatarUrl = userDb.avatarUrl,
            status = UserStatus.OFFLINE,
            userId = userDb.id
        )

}