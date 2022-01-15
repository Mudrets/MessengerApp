package com.example.messengerapp.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.messengerapp.R
import com.example.messengerapp.databinding.ChangeTopicDialogBinding
import com.example.messengerapp.databinding.CreateNewChannelDialogBinding
import com.example.messengerapp.presentation.recyclerview.chat.MessageUi
import com.example.messengerapp.util.ext.fastLazy

class ChangeTopicDialog : DialogFragment() {

    private val resultKey by fastLazy { requireArguments().getString(ARG_RESULT_KEY, "")!! }
    private val message by fastLazy { requireArguments().getParcelable<MessageUi>(MESSAGE)!! }

    private var _binding: ChangeTopicDialogBinding? = null
    val binding: ChangeTopicDialogBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChangeTopicDialogBinding.inflate(inflater, container, false)

        binding.topicNameEditText.setText(
            getString(
                R.string.topic_name_for_stream_chat,
                message.topicName
            )
        )
        binding.changeTopicButton.setOnClickListener {
            val topicName = binding.topicNameEditText.text.toString().trim('#')
            if (topicName.isNotBlank()) {
                setFragmentResult(
                    resultKey, bundleOf(
                        "message" to message,
                        "topicName" to topicName
                    )
                )
                dismiss()
            } else {
                Toast.makeText(context, getString(R.string.blank_topik_warning), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.AlertDialogTheme)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView = View.inflate(context, R.layout.create_new_channel_dialog, null)
        dialog.setContentView(contentView)
    }

    companion object {

        private const val ARG_RESULT_KEY = "RES_KEY"
        private const val MESSAGE = "MESSAGE"

        fun newInstance(resKey: String, messageUi: MessageUi): ChangeTopicDialog {
            return ChangeTopicDialog().apply {
                arguments = bundleOf(
                    ARG_RESULT_KEY to resKey,
                    MESSAGE to messageUi
                )
            }
        }
    }
}