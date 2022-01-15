package com.example.messengerapp.data.data_store

import com.example.messengerapp.data.db.dao.UserDao
import com.example.messengerapp.data.db.mapper.from_db.UserDbToUserMapper
import com.example.messengerapp.data.db.mapper.to_db.UserToUserDbMapper
import com.example.messengerapp.domain.entity.user.User
import com.example.messengerapp.domain.entity.user.UserStatus
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class PeopleDataStoreImpl @Inject constructor(
    private val userDao: UserDao,
    private val userDbToUserMapper: UserDbToUserMapper,
    private val userToUserDbMapper: UserToUserDbMapper
) : PeopleDataStore {
    override fun insertUsers(users: List<User>) {
        userDao.insertUsers(users.map(userToUserDbMapper))
    }

    override fun getAllPeople(): Single<List<User>> =
        userDao.getAllUsers()
            .subscribeOn(Schedulers.io())
            .toObservable()
            .flatMapIterable { it }
            .map(userDbToUserMapper)
            .toList()

    override fun getUserPresence(userId: Long): Single<UserStatus> =
        Single.fromCallable {
            UserStatus.OFFLINE
        }.subscribeOn(Schedulers.io())
}