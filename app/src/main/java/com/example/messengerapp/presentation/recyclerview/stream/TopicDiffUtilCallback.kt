package com.example.messengerapp.presentation.recyclerview.stream

import com.example.messengerapp.presentation.recyclerview.base.ViewTypedDiffUtilCallback

class TopicDiffUtilCallback : ViewTypedDiffUtilCallback<TopicUi>() {

    override fun areContentsTheSame(oldItem: TopicUi, newItem: TopicUi): Boolean =
        oldItem.topicName == newItem.topicName
}