package com.example.messengerapp.data.data_store

import com.example.messengerapp.data.db.dao.UserDao
import com.example.messengerapp.data.db.mapper.from_db.UserDbToUserMapper
import com.example.messengerapp.data.db.mapper.to_db.UserToUserDbMapper
import com.example.messengerapp.domain.entity.user.User
import com.example.messengerapp.util.Constants
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class UserDataStoreImpl @Inject constructor(
    private val userDao: UserDao,
    private val userToUserDbMapper: UserToUserDbMapper,
    private val userDbToUserMapper: UserDbToUserMapper,
) : UserDataStore {
    override fun insertUser(user: User) {
        userDao.insertUser(userToUserDbMapper(user))
    }

    override fun getAuthorizedUser(): Single<User> =
        userDao.getUserByEmail(Constants.DB_EMAIL)
            .subscribeOn(Schedulers.io())
            .map(userDbToUserMapper)

}