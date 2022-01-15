package com.example.messengerapp.domain.repository

import com.example.messengerapp.domain.entity.user.User
import com.example.messengerapp.domain.entity.user.UserStatus
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface PeopleRepository {

    fun loadPeople(): Observable<List<User>>

    fun getUserPresence(userId: Long): Single<UserStatus>

}