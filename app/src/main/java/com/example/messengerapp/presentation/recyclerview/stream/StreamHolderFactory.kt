package com.example.messengerapp.presentation.recyclerview.stream

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.R
import com.example.messengerapp.di.annotation.scope.StreamsFragmentScope
import com.example.messengerapp.presentation.recyclerview.base.BaseViewHolder
import com.example.messengerapp.presentation.recyclerview.base.HolderFactory
import com.example.messengerapp.presentation.recyclerview.base.ShimmerViewHolder
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@StreamsFragmentScope
class StreamHolderFactory @Inject constructor(
    private val topicItemDecoration: RecyclerView.ItemDecoration,
    private val openChatSubject: PublishSubject<Triple<String, Long, String>>
) : HolderFactory() {

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.stream_item -> StreamViewHolder(
                this,
                view,
                topicItemDecoration,
                openChatSubject
            )
            R.layout.topic_item -> TopicViewHolder(view, openChatSubject)
            R.layout.stream_shimmer_item -> ShimmerViewHolder<StreamShimmer>(view)
            else -> null
        }
    }

}