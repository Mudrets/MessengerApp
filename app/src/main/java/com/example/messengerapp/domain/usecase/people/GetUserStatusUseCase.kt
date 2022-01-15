package com.example.messengerapp.domain.usecase.people

import com.example.messengerapp.domain.entity.user.UserStatus
import com.example.messengerapp.domain.repository.PeopleRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface GetUserStatusUseCase : (Long) -> Single<Pair<Long, UserStatus>>

class GetUserStatusUseCaseImpl @Inject constructor(
    private val repository: PeopleRepository
) : GetUserStatusUseCase {

    override fun invoke(userId: Long): Single<Pair<Long, UserStatus>> =
        repository.getUserPresence(userId)
            .map { userId to it }

}