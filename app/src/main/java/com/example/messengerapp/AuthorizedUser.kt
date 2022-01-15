package com.example.messengerapp

import com.example.messengerapp.domain.entity.user.User
import com.example.messengerapp.domain.entity.user.UserStatus
import com.example.messengerapp.domain.usecase.main.GetAuthorizedUserUseCase
import com.example.messengerapp.presentation.recyclerview.user.UserUi
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthorizedUser @Inject constructor(
    private val getAuthorizedUserUseCase: GetAuthorizedUserUseCase,
) {

    var user: User? = null
        private set

    val userUi
        get() = if (user == null)
            UserUi(
                name = "Name",
                email = "email@example.com",
                status = UserStatus.OFFLINE,
                avatarUrl = "",
                uid = "-1"
            )
        else
            UserUi(
                name = user!!.fullName,
                email = user!!.email,
                status = user!!.status,
                avatarUrl = user!!.avatarUrl,
                uid = user!!.userId.toString()
            )

    val userId: Long?
        get() = user?.userId

    fun getUser(): Observable<User> =
        getAuthorizedUserUseCase()
            .doOnNext { userData ->
                if (!isErrorUser(userData))
                    user = userData
            }

    companion object {
        fun isErrorUser(user: User): Boolean =
            user.fullName == "~Database don't have user info~"
                    && user.avatarUrl == "~Database don't have user info~"
                    && user.email == "~Database don't have user info~"
                    && user.userId == -1L
    }
}