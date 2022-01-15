package com.example.messengerapp.data.repository

import com.example.messengerapp.AuthorizedUser
import com.example.messengerapp.data.data_provider.UserDataProvider
import com.example.messengerapp.data.data_store.UserDataStore
import com.example.messengerapp.domain.entity.user.User
import com.example.messengerapp.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataProvider: UserDataProvider,
    private val usersDataStore: UserDataStore
) : UserRepository {

    private var storeIsEmpty = false
    private val delay: Long
        get() = if (storeIsEmpty) SMALL_RETRY_DELAY else BIG_RETRY_DELAY

    override fun getAuthorizedUserData(): Observable<User> {
        val storeObservable = usersDataStore.getAuthorizedUser()
            .onErrorReturn {
                User(
                    fullName = "~Database don't have user info~",
                    email = "~Database don't have user info~",
                    avatarUrl = "~Database don't have user info~",
                    userId = -1
                )
            }
            .doOnSuccess { userData -> storeIsEmpty = AuthorizedUser.isErrorUser(userData) }

        val providerObservable = userDataProvider.getAuthorizedUser()
            .retryWhen { it.delay(delay, TimeUnit.SECONDS) }
            .doOnSuccess { user -> usersDataStore.insertUser(user) }
            .doOnError { Timber.e(it) }

        return storeObservable.mergeWith(providerObservable)
            .toObservable()
            .subscribeOn(Schedulers.computation())
    }

    companion object {
        private const val SMALL_RETRY_DELAY = 5L
        private const val BIG_RETRY_DELAY = 60L
    }
}