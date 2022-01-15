package com.example.messengerapp.presentation.recyclerview.base

import androidx.recyclerview.widget.DiffUtil

abstract class ViewTypedDiffUtilCallback<T : ViewTyped> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem.uid == newItem.uid

    override fun getChangePayload(oldItem: T, newItem: T): Any? = null

}