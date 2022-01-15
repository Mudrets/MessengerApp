package com.example.messengerapp.presentation.dialog.manager

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.example.messengerapp.R
import com.example.messengerapp.presentation.dialog.CreateNewChannelDialog
import com.example.messengerapp.presentation.dialog.command.ChannelsDialogType
import com.example.messengerapp.presentation.dialog.command.DialogCommand

class ChannelsDialogManager(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val tag: String = ""
) : DialogManager<ChannelsDialogType>(fragmentManager, tag) {
    override fun actionTypeToResKey(actionType: ChannelsDialogType): String = when (actionType) {
        ChannelsDialogType.CREATE_STREAM -> CREATE_STREAM_DIALOG_RES
    }

    override fun throwableToMessage(error: Throwable, vararg args: String): Pair<String, String> {
        val title = context.getString(R.string.common_error_title)
        val content = when (error.message) {
            "Channel already exist" -> {
                val streamName = args[0]
                context.getString(R.string.create_already_exists_stream_error_text, streamName)
            }
            else -> {
                val streamName = args[0]
                context.getString(R.string.create_stream_error_text, streamName)
            }
        }
        return title to content
    }

    override fun showDialog(action: DialogCommand<ChannelsDialogType>) = when (action.type) {
        ChannelsDialogType.CREATE_STREAM -> {
            val dialog = CreateNewChannelDialog.newInstance(CREATE_STREAM_DIALOG_RES)
            showDialog(dialog, action.type)
        }
    }

    companion object {
        private const val CREATE_STREAM_DIALOG_RES = "CreateStreamDialogResKey"
    }
}