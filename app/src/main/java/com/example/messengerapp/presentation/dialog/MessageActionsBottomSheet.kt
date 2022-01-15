package com.example.messengerapp.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import com.example.messengerapp.R
import com.example.messengerapp.databinding.MessageActionsBottomSheetDialogBinding
import com.example.messengerapp.domain.entity.chat.MessageType
import com.example.messengerapp.presentation.models.MessageAction
import com.example.messengerapp.presentation.recyclerview.chat.MessageUi
import com.example.messengerapp.util.ext.fastLazy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MessageActionsBottomSheet : BottomSheetDialogFragment() {

    private val resultKey by fastLazy { requireArguments().getString(ARG_RESULT_KEY, "")!! }
    private val messageUi by fastLazy { requireArguments().getParcelable<MessageUi>(MESSAGE)!! }

    private var _binding: MessageActionsBottomSheetDialogBinding? = null
    val binding: MessageActionsBottomSheetDialogBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MessageActionsBottomSheetDialogBinding.inflate(inflater, container, false)

        val isMyMessage = messageUi.messageType == MessageType.OUTGOING_MESSAGE
        binding.deleteMessageButton.isVisible = isMyMessage
        binding.changeTopicMessageButton.isVisible = isMyMessage
        binding.editMessageButton.isVisible = isMyMessage

        binding.addReactionButton.setOnClickListener { sendRes(MessageAction.ADD_REACTION) }
        binding.editMessageButton.setOnClickListener { sendRes(MessageAction.EDIT) }
        binding.changeTopicMessageButton.setOnClickListener { sendRes(MessageAction.CHANGE_TOPIC) }
        binding.copyMessage.setOnClickListener { sendRes(MessageAction.COPY) }
        binding.deleteMessageButton.setOnClickListener { sendRes(MessageAction.DELETE) }

        return binding.root
    }

    private fun sendRes(action: MessageAction) {
        setFragmentResult(
            resultKey, bundleOf(
                "action" to action,
                "message" to messageUi
            )
        )
        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView = View.inflate(context, R.layout.message_actions_bottom_sheet_dialog, null)
        dialog.setContentView(contentView)
    }

    companion object {

        private const val ARG_RESULT_KEY = "ARG_RES_KEY"
        private const val MESSAGE = "MESSAGE"

        fun newInstance(resultKey: String, messageUi: MessageUi): MessageActionsBottomSheet {
            return MessageActionsBottomSheet().apply {
                arguments = bundleOf(
                    ARG_RESULT_KEY to resultKey,
                    MESSAGE to messageUi
                )
            }
        }
    }

}