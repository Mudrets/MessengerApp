package com.example.messengerapp.presentation.recyclerview.stream

import android.os.Bundle
import com.example.messengerapp.di.annotation.scope.StreamsFragmentScope
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped
import com.example.messengerapp.presentation.recyclerview.base.ViewTypedDiffUtilCallback
import javax.inject.Inject

@StreamsFragmentScope
class StreamDiffUtilCallback @Inject constructor() : ViewTypedDiffUtilCallback<ViewTyped>() {

    override fun areContentsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean {
        return if (oldItem is StreamUi && newItem is StreamUi)
            (oldItem.streamName == newItem.streamName
                    && oldItem.items == newItem.items)
        else
            oldItem is StreamShimmer && newItem is StreamShimmer
    }

    override fun getChangePayload(oldItem: ViewTyped, newItem: ViewTyped): Any {
        val changeBundle = Bundle()
        if (oldItem is StreamUi && newItem is StreamUi) {
            if (oldItem.streamName != newItem.streamName) {
                changeBundle.putString("streamName", newItem.streamName)
            }
            if (oldItem.items != newItem.items) {
                changeBundle.putParcelableArrayList("items", ArrayList(newItem.items))
            }
        }
        return changeBundle
    }

}