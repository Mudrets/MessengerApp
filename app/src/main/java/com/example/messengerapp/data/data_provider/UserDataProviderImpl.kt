package com.example.messengerapp.data.data_provider

import com.example.messengerapp.data.network.ZulipApi
import com.example.messengerapp.data.network.mapper.UserPresenceToStatusMapper
import com.example.messengerapp.domain.entity.user.User
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class UserDataProviderImpl @Inject constructor(
    private val zulipApi: ZulipApi,
    private val userPresenceToStatusMapper: UserPresenceToStatusMapper
) : UserDataProvider {
    override fun getAuthorizedUser(): Single<User> =
        zulipApi.getAuthorizedUser()
            .subscribeOn(Schedulers.io())
            .concatMap { networkUser ->
                zulipApi.getUserPresence(networkUser.userId)
                    .subscribeOn(Schedulers.io())
                    .map { presence ->
                        val status = userPresenceToStatusMapper(presence)
                        User(
                            fullName = networkUser.fullName,
                            email = networkUser.email,
                            avatarUrl = networkUser.avatarUrl,
                            status = status,
                            userId = networkUser.userId
                        )
                    }
            }
}