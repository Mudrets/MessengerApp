package com.example.messengerapp.domain.repository

import com.example.messengerapp.domain.entity.user.User
import io.reactivex.rxjava3.core.Observable

interface UserRepository {

    fun getAuthorizedUserData(): Observable<User>

}