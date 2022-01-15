package com.example.messengerapp.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.messengerapp.R
import com.example.messengerapp.domain.entity.chat.Emoji
import com.example.messengerapp.presentation.view.EmojiView
import com.example.messengerapp.presentation.view_group.FlexBox
import com.example.messengerapp.presentation.view_group.message.EmojiUi
import com.example.messengerapp.util.ext.fastLazy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EmojiBottomSheet : BottomSheetDialogFragment() {

    private val resultKey by fastLazy { requireArguments().getString(ARG_RESULT_KEY, "")!! }
    private val messageId by fastLazy { requireArguments().getLong(MESSAGE_ID_KEY) }
    private val emojiList by fastLazy {
        requireArguments().getParcelableArrayList<EmojiUi>(
            EMOJI_LIST
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.emoji_bottom_sheet, container, false)
        val flexBox = view.findViewById<FlexBox>(R.id.emojiFlexBox)
        if (emojiList != null) {
            for (emoji in emojiList!!) {
                val emojiView = EmojiView(requireContext())
                emojiView.sizeOfEmoji = resources.getDimension(R.dimen.emoji_in_bottom_sheet_size)
                emojiView.showNumber = false
                emojiView.emoji = emoji
                flexBox.addView(emojiView)
            }
        }

        flexBox.setChildrenOnClickListener { emojiView ->
            setFragmentResult(
                resultKey, bundleOf(
                    "selectedEmojiName" to (emojiView as? EmojiView)?.emojiName,
                    "messageId" to messageId
                )
            )
            dismiss()
        }

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView = View.inflate(context, R.layout.emoji_bottom_sheet, null)
        dialog.setContentView(contentView)
    }

    companion object {

        private const val ARG_RESULT_KEY = "ARG_RESULT_KEY"
        private const val MESSAGE_ID_KEY = "MESSAGE_ID"
        private const val EMOJI_LIST = "EMOJI_LIST"

        fun newInstance(
            resultKey: String,
            messageId: Long,
            emojiList: List<EmojiUi> = listOf()
        ): EmojiBottomSheet {
            return EmojiBottomSheet().apply {
                arguments = bundleOf(
                    ARG_RESULT_KEY to resultKey,
                    MESSAGE_ID_KEY to messageId,
                    EMOJI_LIST to emojiList
                )
            }
        }

    }
}