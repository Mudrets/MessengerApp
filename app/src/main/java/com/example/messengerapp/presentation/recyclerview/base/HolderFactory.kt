package com.example.messengerapp.presentation.recyclerview.base

import android.view.View
import android.view.ViewGroup
import com.example.messengerapp.R
import com.example.messengerapp.util.ext.inflate

abstract class HolderFactory : (ViewGroup, Int) -> BaseViewHolder<ViewTyped> {

    /**
     * Создает инстанс [BaseViewHolder]-а
     */
    abstract fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>?

    final override fun invoke(viewGroup: ViewGroup, viewType: Int): BaseViewHolder<ViewTyped> {
        val view: View = viewGroup.inflate(viewType)
        return when (viewType) {
            R.layout.item_loader -> BaseViewHolder<NextItemLoader>(view)
            else -> checkNotNull(createViewHolder(view, viewType)) {
                "unknown viewType=" + viewGroup.resources.getResourceName(viewType)
            }
        } as BaseViewHolder<ViewTyped>
    }

}