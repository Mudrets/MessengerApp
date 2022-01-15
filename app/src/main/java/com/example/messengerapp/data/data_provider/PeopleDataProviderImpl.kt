package com.example.messengerapp.data.data_provider

import com.example.messengerapp.data.network.ZulipApi
import com.example.messengerapp.data.network.mapper.NetworkUserToUserMapper
import com.example.messengerapp.data.network.mapper.UserPresenceToStatusMapper
import com.example.messengerapp.domain.entity.user.User
import com.example.messengerapp.domain.entity.user.UserStatus
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Provider

class PeopleDataProviderImpl @Inject constructor(
    private val zulipApi: ZulipApi,
    private val networkUserMapper: NetworkUserToUserMapper,
    private val userPresenceToStatus: UserPresenceToStatusMapper,
    private val authorizedUserId: Provider<Long>
) : PeopleDataProvider {
    override fun getAllPeople(): Single<List<User>> =
        zulipApi.getUsers()
            .subscribeOn(Schedulers.io())
            .toObservable()
            .flatMapIterable { it.members }
            .filter { user -> !user.isBot && user.isActive && user.userId != authorizedUserId.get() }
            .map(networkUserMapper)
            .toList()

    override fun getUserPresence(userId: Long): Single<UserStatus> =
        zulipApi.getUserPresence(userId)
            .subscribeOn(Schedulers.io())
            .map { presence ->
                userPresenceToStatus(presence)
            }
}