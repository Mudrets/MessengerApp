package com.example.messengerapp.presentation.recyclerview.chat

import android.view.View
import com.example.messengerapp.R
import com.example.messengerapp.di.annotation.scope.ChatFragmentScope
import com.example.messengerapp.presentation.models.ChangeMessageReactionRequest
import com.example.messengerapp.presentation.recyclerview.base.BaseViewHolder
import com.example.messengerapp.presentation.recyclerview.base.HolderFactory
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@ChatFragmentScope
class ChatHolderFactory @Inject constructor(
    private val topicDelimiterClickSubject: PublishSubject<String>,
    private val changeReactionSubject: PublishSubject<ChangeMessageReactionRequest>,
    private val selectMessageActionSubject: PublishSubject<MessageUi>,
    private val selectReactionSubject: PublishSubject<Long>
) : HolderFactory() {

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.message_item -> MessageViewHolder(
                view,
                changeReactionSubject,
                selectMessageActionSubject,
                selectReactionSubject
            )
            R.layout.date_item -> DateViewHolder(view)
            R.layout.topic_delimiter -> TopicDelimiterViewHolder(view, topicDelimiterClickSubject)
            else -> null
        }
    }

}