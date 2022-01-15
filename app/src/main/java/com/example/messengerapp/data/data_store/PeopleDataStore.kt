package com.example.messengerapp.data.data_store

import com.example.messengerapp.data.data_provider.PeopleDataProvider
import com.example.messengerapp.domain.entity.user.User

interface PeopleDataStore : PeopleDataProvider {
    fun insertUsers(users: List<User>)
}