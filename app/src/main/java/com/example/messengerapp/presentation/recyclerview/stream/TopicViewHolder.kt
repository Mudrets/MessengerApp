package com.example.messengerapp.presentation.recyclerview.stream

import android.os.Parcelable
import android.view.View
import android.widget.TextView
import com.example.messengerapp.R
import com.example.messengerapp.presentation.recyclerview.base.BaseViewHolder
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.parcelize.Parcelize

@Parcelize
class TopicUi(
    val topicName: String,
    val streamName: String,
    val streamId: Long,
    val color: Int,
    override val viewType: Int = R.layout.topic_item,
) : ViewTyped, Parcelable

class TopicViewHolder(
    val view: View,
    private val openChatSubject: PublishSubject<Triple<String, Long, String>>
) : BaseViewHolder<TopicUi>(view) {

    private val topicNameTextView: TextView = view.findViewById(R.id.topicName)
    private val messagesCountTextView: TextView = view.findViewById(R.id.countMessagesInTopic)

    override fun bind(item: TopicUi) {
        topicNameTextView.text = item.topicName
        messagesCountTextView.text = ""
        view.setBackgroundColor(view.context.getColor(item.color))
        view.setOnClickListener {
            openChatSubject.onNext(
                Triple(
                    item.streamName,
                    item.streamId,
                    item.topicName
                )
            )
        }
    }

}