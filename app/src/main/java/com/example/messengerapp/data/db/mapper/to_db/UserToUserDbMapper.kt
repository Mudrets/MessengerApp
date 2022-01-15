package com.example.messengerapp.data.db.mapper.to_db

import com.example.messengerapp.data.db.entities.UserDb
import com.example.messengerapp.domain.entity.user.User
import javax.inject.Inject

interface UserToUserDbMapper : (User) -> UserDb

class UserToUserDbMapperImpl @Inject constructor() : UserToUserDbMapper {
    override fun invoke(user: User): UserDb =
        UserDb(
            id = user.userId,
            avatarUrl = user.avatarUrl,
            email = user.email,
            fullName = user.fullName
        )

}