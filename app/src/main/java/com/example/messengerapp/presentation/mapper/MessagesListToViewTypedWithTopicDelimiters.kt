package com.example.messengerapp.presentation.mapper

import com.example.messengerapp.domain.entity.chat.Message
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped
import com.example.messengerapp.presentation.recyclerview.chat.DateUi
import com.example.messengerapp.presentation.recyclerview.chat.TopicDelimiter
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MessagesListToViewTypedWithTopicDelimiters @Inject constructor(
    private val messageToMessageUiMapper: MessageToMessageUiMapper
) : MessagesListToViewTypedListMapper {

    private var uidNum = 0L

    private var prevTopicName = ""

    override fun invoke(messages: List<Message>): List<ViewTyped> =
        messages.sortedByDescending(Message::date).groupBy {
            SimpleDateFormat("d MMM", Locale("ru")).format(it.date)
        }.flatMap { (date, messagesList) ->
            var (day, month) = date.split(' ')
            month = month.substring(0, 3).capitalize(Locale("ru"))
            val resDate = "$day $month"
            val resMessages = messagesList.groupBy(
                { message ->
                    val topicName = message.topicName
                    if (topicName != prevTopicName) {
                        prevTopicName = topicName
                        uidNum = message.id
                    }
                    val uid = "$topicName.${uidNum}"
                    TopicDelimiter(
                        topicName = topicName,
                        uid = uid
                    )
                },
                { message -> messageToMessageUiMapper(message) }
            ).flatMap { (topicDelimiter, messages) ->
                messages + topicDelimiter
            }
            resMessages + DateUi(date = resDate)
        }.reversed().distinctBy { viewTyped ->
            viewTyped.uid
        }.reversed()
}