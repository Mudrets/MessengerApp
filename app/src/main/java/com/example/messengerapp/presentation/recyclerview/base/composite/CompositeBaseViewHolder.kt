package com.example.messengerapp.presentation.recyclerview.base.composite

import android.view.View
import com.example.messengerapp.presentation.recyclerview.base.BaseViewHolder
import com.example.messengerapp.presentation.recyclerview.base.HolderFactory

open class CompositeBaseViewHolder<T : CompositeViewTyped>(
    containerView: View
) : BaseViewHolder<T>(containerView) {

    open val holderFactory: HolderFactory
        get() = error("provide holderFactory $this")

}