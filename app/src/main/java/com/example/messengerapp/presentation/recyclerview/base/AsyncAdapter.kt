package com.example.messengerapp.presentation.recyclerview.base

import androidx.recyclerview.widget.AsyncListDiffer

open class AsyncAdapter<T : ViewTyped>(
    holderFactory: HolderFactory,
    diffUtilCallback: ViewTypedDiffUtilCallback<T>
) : BaseAdapter<T>(holderFactory) {

    protected val differ = AsyncListDiffer(this, diffUtilCallback)

    override var items: List<T>
        get() = differ.currentList
        set(newItems) = differ.submitList(newItems)
}