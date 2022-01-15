package com.example.messengerapp.presentation.recyclerview.user

import com.example.messengerapp.R
import com.example.messengerapp.presentation.recyclerview.base.ShimmerViewTyped

data class PeopleShimmer(
    override val containerId: Int = R.id.userShimmerContainer,
    override val viewType: Int = R.layout.user_shimmer_item
) : ShimmerViewTyped
