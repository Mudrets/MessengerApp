package com.example.messengerapp.data.data_provider

import com.example.messengerapp.domain.entity.user.User
import com.example.messengerapp.domain.entity.user.UserStatus
import io.reactivex.rxjava3.core.Single

interface PeopleDataProvider {
    fun getAllPeople(): Single<List<User>>
    fun getUserPresence(userId: Long): Single<UserStatus>
}