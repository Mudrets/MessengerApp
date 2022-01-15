package com.example.messengerapp.presentation.recyclerview.chat

import com.example.messengerapp.presentation.recyclerview.base.AsyncAdapter
import com.example.messengerapp.presentation.recyclerview.base.HolderFactory
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped
import com.example.messengerapp.presentation.recyclerview.base.ViewTypedDiffUtilCallback

class ChatAsyncAdapter(
    holderFactory: HolderFactory,
    diffUtilCallback: ViewTypedDiffUtilCallback<ViewTyped>
) : AsyncAdapter<ViewTyped>(holderFactory, diffUtilCallback) {

    var addNewElementToStartCallback: () -> Unit = {}

    override var items: List<ViewTyped>
        get() = differ.currentList
        set(newItems) {
            val oldList = differ.currentList
            differ.submitList(newItems) {
                if (oldList.isNotEmpty()) {
                    val secondMessage = oldList.subList(1, oldList.size).find { it is MessageUi }
                    if (newItems.isNotEmpty() && secondMessage != null
                        && oldList[0] != newItems[0] && secondMessage != newItems[0]
                    )
                        addNewElementToStartCallback()
                }
            }
        }
}