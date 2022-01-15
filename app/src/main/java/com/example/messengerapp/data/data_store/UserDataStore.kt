package com.example.messengerapp.data.data_store

import com.example.messengerapp.data.data_provider.UserDataProvider
import com.example.messengerapp.domain.entity.user.User

interface UserDataStore : UserDataProvider {
    fun insertUser(user: User)
}