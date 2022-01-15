package com.example.messengerapp.presentation.recyclerview.chat

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import com.example.messengerapp.R
import com.example.messengerapp.domain.entity.chat.MessageType
import com.example.messengerapp.domain.entity.chat.SendingStatus
import com.example.messengerapp.presentation.models.ChangeMessageReactionRequest
import com.example.messengerapp.presentation.models.ChangeReactionType
import com.example.messengerapp.presentation.recyclerview.base.BaseViewHolder
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped
import com.example.messengerapp.presentation.view_group.message.EmojiUi
import com.example.messengerapp.presentation.view_group.message.MessageViewGroup
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageUi(
    val messageText: String,
    val senderName: String,
    val messageType: MessageType,
    val topicName: String,
    val avatarUrl: String = "",
    val emojiList: List<EmojiUi> = listOf(),
    val messageId: Long = -1,
    val sendingStatus: SendingStatus = SendingStatus.SUCCESS,
    override val viewType: Int = R.layout.message_item,
    override val uid: String = messageId.toString()
) : ViewTyped, Parcelable {

    fun addEmoji(emojiName: String, status: SendingStatus): MessageUi {
        val newEmojiList = mutableListOf<EmojiUi>()
        newEmojiList.addAll(emojiList)
        val index = emojiList.indexOfFirst { it.emojiName == emojiName }
        if (index != -1) {
            newEmojiList[index] = newEmojiList[index].select()
        } else {
            newEmojiList.add(EmojiUi.firstEmoji(emojiName))
        }
        return copy(emojiList = newEmojiList, sendingStatus = status)
    }

    fun removeEmoji(emojiName: String, status: SendingStatus): MessageUi {
        val newEmojiList = mutableListOf<EmojiUi>()
        newEmojiList.addAll(emojiList)
        val index = emojiList.indexOfFirst { it.emojiName == emojiName }
        val changedEmoji = emojiList[index].removeSelection()
        if (changedEmoji.count > 0) {
            newEmojiList[index] = changedEmoji
            false to index
        } else {
            newEmojiList.removeAt(index)
        }
        return copy(emojiList = newEmojiList, sendingStatus = status)
    }
}

class MessageViewHolder(
    private val view: View,
    private val changeReactionSubject: PublishSubject<ChangeMessageReactionRequest>,
    private val selectMessageActionSubject: PublishSubject<MessageUi>,
    private val selectReactionSubject: PublishSubject<Long>
) : BaseViewHolder<MessageUi>(view) {

    private var currMessageId = -1L
    private var currMessageText = ""
    private val messageHolder: MessageViewGroup = view.findViewById(R.id.messageRow)

    override fun bind(item: MessageUi) {
        currMessageId = item.messageId
        currMessageText = item.messageText
        messageHolder.message = item
        setListeners(item)
    }

    override fun bind(item: MessageUi, payload: List<Any>) {
        val bundlePayload = payload[0] as? Bundle ?: Bundle()
        if (bundlePayload.containsKey("emojiList")) {
            val newEmojiList = bundlePayload.getParcelableArrayList<EmojiUi>("emojiList")
            if (newEmojiList != null)
                messageHolder.setNewEmojiList(newEmojiList)
        }
        if (bundlePayload.containsKey("sendingStatus")) {
            val sendingStatus = bundlePayload.getParcelable<SendingStatus>("sendingStatus")
            if (sendingStatus != null) {
                messageHolder.isProgressBarVisible = sendingStatus == SendingStatus.IS_SENDING
                messageHolder.isWarningVisible = sendingStatus == SendingStatus.ERROR
            }
        }
        if (bundlePayload.containsKey("messageId")) {
            val messageId = bundlePayload.getLong("messageId")
            currMessageId = messageId
        }
        if (bundlePayload.containsKey("messageText")) {
            val messageText = bundlePayload.getString("messageText")!!
            currMessageText = messageText
            messageHolder.messageText = messageText
        }
    }

    private fun setListeners(item: MessageUi) {
        messageHolder.addEmojiButtonOnClickListener =
            { selectReactionSubject.onNext(currMessageId) }

        messageHolder.messageOnLongClickListener =
            {
                selectMessageActionSubject.onNext(
                    item.copy(
                        messageId = currMessageId,
                        messageText = currMessageText
                    )
                )
            }

        messageHolder.emojiOnClickListener = { emojiView ->
            val changeType = if (emojiView.isSelected) {
                ChangeReactionType.REMOVE
            } else {
                ChangeReactionType.ADD
            }

            val change = ChangeMessageReactionRequest(
                emojiName = emojiView.emojiName,
                messageId = currMessageId,
                actionType = changeType
            )
            changeReactionSubject.onNext(change)
        }
    }
}