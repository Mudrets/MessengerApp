package com.example.messengerapp.presentation.recyclerview.user

import android.view.View
import com.example.messengerapp.R
import com.example.messengerapp.di.annotation.scope.PeopleFragmentScope
import com.example.messengerapp.presentation.recyclerview.base.BaseViewHolder
import com.example.messengerapp.presentation.recyclerview.base.HolderFactory
import com.example.messengerapp.presentation.recyclerview.base.ShimmerViewHolder
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@PeopleFragmentScope
class UserHolderFactory @Inject constructor(
    private val userSubject: PublishSubject<UserUi>,
    private val userStatusSubject: PublishSubject<Long>
) : HolderFactory() {

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.user_item -> UserViewHolder(
                view,
                userSubject,
                userStatusSubject
            )
            R.layout.user_shimmer_item -> ShimmerViewHolder<PeopleShimmer>(view)
            else -> null
        }
    }

}