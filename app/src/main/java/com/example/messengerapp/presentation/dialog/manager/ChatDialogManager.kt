package com.example.messengerapp.presentation.dialog.manager

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.example.messengerapp.EmojiInfo
import com.example.messengerapp.R
import com.example.messengerapp.presentation.dialog.ChangeTopicDialog
import com.example.messengerapp.presentation.dialog.EmojiBottomSheet
import com.example.messengerapp.presentation.dialog.MessageActionsBottomSheet
import com.example.messengerapp.presentation.dialog.command.ChatDialogType
import com.example.messengerapp.presentation.dialog.command.DialogCommand
import com.example.messengerapp.presentation.recyclerview.chat.MessageUi
import timber.log.Timber

class ChatDialogManager(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val emojiInfo: EmojiInfo,
    private val tag: String = ""
) : DialogManager<ChatDialogType>(fragmentManager, tag) {

    private fun log(actionType: ChatDialogType) {
        Timber.e("Data in bundle was null when ${actionType.name} was started")
    }

    override fun actionTypeToResKey(actionType: ChatDialogType): String = when (actionType) {
        ChatDialogType.MESSAGE_ACTION -> MESSAGE_ACTIONS_BOTTOM_SHEET_RES
        ChatDialogType.EMOJI_PICKER -> EMOJI_BOTTOM_SHEET_RES
        ChatDialogType.CHANGE_TOPIC -> CHANGE_TOPIC_DIALOG_RES
    }

    override fun throwableToMessage(error: Throwable, vararg args: String): Pair<String, String> {
        val title = context.getString(R.string.common_error_title)
        val text: String = when (error.message) {
            "delete message" -> context.getString(R.string.delete_message_error_text)
            "remove reaction" -> context.getString(R.string.delete_reaction_error_text)
            "add reaction" -> context.getString(R.string.add_reaction_error_text)
            "You don't have permission to delete this message" ->
                context.getString(R.string.dont_have_permissions_for_delete_error_text)
            else -> context.getString(R.string.common_error_text)
        }
        return title to text
    }

    override fun showDialog(action: DialogCommand<ChatDialogType>) = when (action.type) {
        ChatDialogType.MESSAGE_ACTION -> {
            val messageUi = action.data.getParcelable<MessageUi>(ACTION_DATA_KEY)
            if (messageUi != null) {
                val dialog = MessageActionsBottomSheet.newInstance(
                    MESSAGE_ACTIONS_BOTTOM_SHEET_RES,
                    messageUi
                )
                showDialog(dialog, action.type)
            } else {
                log(action.type)
            }
        }
        ChatDialogType.EMOJI_PICKER -> {
            val messageId = action.data.getLong(ACTION_DATA_KEY)
            val emojiList = emojiInfo.getEmojiList()
            val dialog = EmojiBottomSheet.newInstance(EMOJI_BOTTOM_SHEET_RES, messageId, emojiList)
            showDialog(dialog, action.type)
        }
        ChatDialogType.CHANGE_TOPIC -> {
            val messageUi = action.data.getParcelable<MessageUi>(ACTION_DATA_KEY)
            if (messageUi != null) {
                val dialog = ChangeTopicDialog.newInstance(CHANGE_TOPIC_DIALOG_RES, messageUi)
                showDialog(dialog, action.type)
            } else {
                log(action.type)
            }
        }
    }

    companion object {
        private const val EMOJI_BOTTOM_SHEET_RES = "EmojiBottomSheetRes"
        private const val MESSAGE_ACTIONS_BOTTOM_SHEET_RES = "MessageActionsBottomSheetRes"
        private const val CHANGE_TOPIC_DIALOG_RES = "ChangeTopicDialog"
    }
}