package com.example.messengerapp.domain.usecase.main

import com.example.messengerapp.domain.entity.user.User
import com.example.messengerapp.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

interface GetAuthorizedUserUseCase : () -> Observable<User>

@Singleton
class GetAuthorizedUserUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : GetAuthorizedUserUseCase {

    override fun invoke(): Observable<User> =
        repository.getAuthorizedUserData()

}