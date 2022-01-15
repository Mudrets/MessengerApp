package com.example.messengerapp.presentation.recyclerview.base

import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout

interface ShimmerViewTyped : ViewTyped {
    override val uid: String
        get() = "shimmer"

    val containerId: Int
}

open class ShimmerViewHolder<T : ShimmerViewTyped>(
    val view: View
) : BaseViewHolder<T>(view) {

    override fun bind(item: T) {
        val shimmerContainer = view.findViewById<ShimmerFrameLayout>(item.containerId)
        shimmerContainer.startShimmer()
    }

}

