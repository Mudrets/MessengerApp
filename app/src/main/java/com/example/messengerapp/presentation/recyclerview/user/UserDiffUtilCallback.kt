package com.example.messengerapp.presentation.recyclerview.user

import android.os.Bundle
import com.example.messengerapp.di.annotation.scope.PeopleFragmentScope
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped
import com.example.messengerapp.presentation.recyclerview.base.ViewTypedDiffUtilCallback
import javax.inject.Inject

@PeopleFragmentScope
class UserDiffUtilCallback @Inject constructor() : ViewTypedDiffUtilCallback<ViewTyped>() {

    override fun areContentsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean =
        if (oldItem is UserUi && newItem is UserUi)
            oldItem.name == newItem.name && oldItem.email == newItem.email
                    && oldItem.status == newItem.status
        else
            oldItem is PeopleShimmer && newItem is PeopleShimmer


    override fun getChangePayload(oldItem: ViewTyped, newItem: ViewTyped): Any {
        val changeBundle = Bundle()
        if (oldItem is UserUi && newItem is UserUi) {
            if (oldItem.name != newItem.name) {
                changeBundle.putString("name", newItem.name)
            }
            if (oldItem.email != newItem.email) {
                changeBundle.putString("email", newItem.email)
            }
            if (oldItem.status != newItem.status) {
                changeBundle.putParcelable("status", newItem.status)
            }
        }
        return changeBundle
    }

}