package com.example.messengerapp.presentation.recyclerview.chat

import android.os.Bundle
import com.example.messengerapp.di.annotation.scope.ChatFragmentScope
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped
import com.example.messengerapp.presentation.recyclerview.base.ViewTypedDiffUtilCallback
import javax.inject.Inject

@ChatFragmentScope
class ChatDiffUtilCallback @Inject constructor() : ViewTypedDiffUtilCallback<ViewTyped>() {

    override fun areContentsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean {
        if (oldItem is MessageUi && newItem is MessageUi) {
            return oldItem.messageText == newItem.messageText &&
                    oldItem.emojiList == newItem.emojiList &&
                    oldItem.senderName == newItem.senderName &&
                    oldItem.sendingStatus == newItem.sendingStatus
        } else if (oldItem is DateUi && newItem is DateUi) {
            return oldItem.date == newItem.date
        } else if (oldItem is TopicDelimiter && newItem is TopicDelimiter) {
            return oldItem.topicName == newItem.topicName
        }
        return false
    }

    override fun getChangePayload(oldItem: ViewTyped, newItem: ViewTyped): Any {
        val changeBundle = Bundle()
        if (oldItem is MessageUi && newItem is MessageUi) {
            if (oldItem.emojiList != newItem.emojiList) {
                changeBundle.putParcelableArrayList("emojiList", ArrayList(newItem.emojiList))
            }
            if (oldItem.sendingStatus != newItem.sendingStatus) {
                changeBundle.putParcelable("sendingStatus", newItem.sendingStatus)
            }
            if (oldItem.messageId != newItem.messageId) {
                changeBundle.putLong("messageId", newItem.messageId)
            }
            if (oldItem.messageText != newItem.messageText) {
                changeBundle.putString("messageText", newItem.messageText)
            }
        } else if (oldItem is TopicDelimiter && newItem is TopicDelimiter) {
            if (oldItem.topicName != newItem.topicName)
                changeBundle.putString("topicName", newItem.topicName)
        }
        return changeBundle
    }
}