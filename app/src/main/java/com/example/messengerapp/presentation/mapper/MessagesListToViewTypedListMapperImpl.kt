package com.example.messengerapp.presentation.mapper

import com.example.messengerapp.domain.entity.chat.Message
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped
import com.example.messengerapp.presentation.recyclerview.chat.DateUi
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MessagesListToViewTypedListMapperImpl @Inject constructor(
    private val messageToMessageUiMapper: MessageToMessageUiMapper
) : MessagesListToViewTypedListMapper {
    override fun invoke(messages: List<Message>): List<ViewTyped> =
        messages.sortedByDescending(Message::date).groupBy {
            SimpleDateFormat("d MMM", Locale("ru")).format(it.date)
        }.flatMap { (date, messagesList) ->
            var (day, month) = date.split(' ')
            month = month.substring(0, 3).capitalize(Locale("ru"))
            val resDate = "$day $month"
            val list = mutableListOf<ViewTyped>()
            list.addAll(messagesList.map(messageToMessageUiMapper))
            list.add(DateUi(resDate))
            list
        }

}