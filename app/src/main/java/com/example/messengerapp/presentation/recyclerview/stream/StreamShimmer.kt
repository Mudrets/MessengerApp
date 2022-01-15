package com.example.messengerapp.presentation.recyclerview.stream

import com.example.messengerapp.R
import com.example.messengerapp.presentation.recyclerview.base.ShimmerViewTyped

data class StreamShimmer(
    override val containerId: Int = R.id.streamShimmerFrame,
    override val viewType: Int = R.layout.stream_shimmer_item
) : ShimmerViewTyped
