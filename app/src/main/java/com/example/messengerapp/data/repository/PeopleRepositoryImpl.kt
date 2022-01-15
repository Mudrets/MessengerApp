package com.example.messengerapp.data.repository

import com.example.messengerapp.data.data_provider.PeopleDataProvider
import com.example.messengerapp.data.data_store.PeopleDataStore
import com.example.messengerapp.domain.entity.user.User
import com.example.messengerapp.domain.entity.user.UserStatus
import com.example.messengerapp.domain.repository.PeopleRepository
import com.example.messengerapp.util.ext.retryWithDelay
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(
    private val peopleDataProvider: PeopleDataProvider,
    private val peopleDataStore: PeopleDataStore
) : PeopleRepository {

    override fun loadPeople(): Observable<List<User>> {
        val dataObservable = peopleDataStore.getAllPeople()
            .onErrorReturn { listOf() }
        val providerObservable = peopleDataProvider.getAllPeople()
            .retryWithDelay(TIMES, RETRY_DELAY)
            .doOnSuccess { people -> peopleDataStore.insertUsers(people) }
            .doOnError { Timber.e(it) }

        return dataObservable.mergeWith(providerObservable)
            .toObservable()
            .observeOn(Schedulers.computation())
    }

    override fun getUserPresence(userId: Long): Single<UserStatus> =
        peopleDataProvider.getUserPresence(userId)

    companion object {
        private const val RETRY_DELAY = 3L
        private const val TIMES = 3
    }

}