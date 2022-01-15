package com.example.messengerapp.data.data_provider

import com.example.messengerapp.domain.entity.user.User
import io.reactivex.rxjava3.core.Single

interface UserDataProvider {
    fun getAuthorizedUser(): Single<User>
}