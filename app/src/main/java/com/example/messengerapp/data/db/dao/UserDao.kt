package com.example.messengerapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.messengerapp.data.db.entities.UserDb
import io.reactivex.rxjava3.core.Single

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(userEntity: UserDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(userEntities: List<UserDb>)

    @Query("SELECT * FROM UserDb")
    fun getAllUsers(): Single<List<UserDb>>

    @Query("SELECT * FROM UserDb WHERE email = :email")
    fun getUserByEmail(email: String): Single<UserDb>
}