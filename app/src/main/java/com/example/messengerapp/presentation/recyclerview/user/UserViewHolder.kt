package com.example.messengerapp.presentation.recyclerview.user

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import com.example.messengerapp.R
import com.example.messengerapp.domain.entity.user.UserStatus
import com.example.messengerapp.presentation.recyclerview.base.BaseViewHolder
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped
import com.example.messengerapp.presentation.view_group.UserView
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserUi(
    val name: String,
    val email: String,
    val status: UserStatus = UserStatus.OFFLINE,
    val avatarUrl: String? = null,
    override val viewType: Int = R.layout.user_item,
    override val uid: String
) : ViewTyped, Parcelable

class UserViewHolder(
    val view: View,
    private val userSubject: PublishSubject<UserUi>,
    private val userStatusSubject: PublishSubject<Long>

) : BaseViewHolder<UserUi>(view) {

    private var currStatus: UserStatus = UserStatus.OFFLINE
    private val userView: UserView = view as UserView

    override fun bind(item: UserUi) {
        currStatus = item.status
        userView.user = item
        userView.setOnClickListener {
            userSubject.onNext(item.copy(status = currStatus))
        }
        userStatusSubject.onNext(item.uid.toLong())
    }

    override fun bind(item: UserUi, payload: List<Any>) {
        userStatusSubject.onNext(item.uid.toLong())
        val changeBundle = payload[0] as? Bundle ?: Bundle()
        userView.setOnClickListener {
            userSubject.onNext(item)
        }
        if (changeBundle.containsKey("name")) {
            userView.userName = changeBundle.getString("name") ?: item.name
        }
        if (changeBundle.containsKey("email")) {
            userView.email = changeBundle.getString("email") ?: item.email
        }
        if (changeBundle.containsKey("status")) {
            currStatus = item.status
            userView.status = changeBundle.getParcelable("status") ?: UserStatus.OFFLINE
        }
    }
}