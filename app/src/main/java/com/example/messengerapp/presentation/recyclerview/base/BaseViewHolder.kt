package com.example.messengerapp.presentation.recyclerview.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder<T : ViewTyped>(
    containerView: View
) : RecyclerView.ViewHolder(containerView) {

    /**
     * Связавыет переданный элемент с [View]
     *
     * @param item элемент, содержащий данные, которые необходимо отобразить
     */
    open fun bind(item: T) = Unit

    /**
     * Связывает переданный элемент с [View]
     *
     * @param item элемент, содержащий данные, которые необходимо отобразить
     * @param payload список измененных элементов
     */
    open fun bind(item: T, payload: List<Any>) = Unit
}