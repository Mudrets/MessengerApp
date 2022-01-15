package com.example.messengerapp.presentation.recyclerview.chat

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.messengerapp.R
import com.example.messengerapp.presentation.recyclerview.base.BaseViewHolder
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped
import io.reactivex.rxjava3.subjects.PublishSubject

data class TopicDelimiter(
    val topicName: String,
    override val viewType: Int = R.layout.topic_delimiter,
    override val uid: String
) : ViewTyped

class TopicDelimiterViewHolder(
    private val view: View,
    private val clickSubject: PublishSubject<String>,
) : BaseViewHolder<TopicDelimiter>(view) {

    private val topicNameTextView = view.findViewById<TextView>(R.id.topicDelimiterName)

    override fun bind(item: TopicDelimiter) {
        topicNameTextView.text = view.resources.getString(R.string.topic_delimiter, item.topicName)
        view.setOnClickListener {
            clickSubject.onNext(item.topicName)
        }
    }

    override fun bind(item: TopicDelimiter, payload: List<Any>) {
        val bundlePayload = payload[0] as? Bundle ?: Bundle()
        if (bundlePayload.containsKey("topicName")) {
            topicNameTextView.text = bundlePayload.getString("topicName")
        }
    }
}