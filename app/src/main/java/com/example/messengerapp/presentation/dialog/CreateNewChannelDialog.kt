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
import com.example.messengerapp.databinding.CreateNewChannelDialogBinding
import com.example.messengerapp.util.ext.fastLazy


class CreateNewChannelDialog : DialogFragment() {

    private val resultKey by fastLazy { requireArguments().getString(ARG_RESULT_KEY, "")!! }

    private var _binding: CreateNewChannelDialogBinding? = null
    val binding: CreateNewChannelDialogBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreateNewChannelDialogBinding.inflate(inflater, container, false)

        binding.streamNameEditText.doAfterTextChanged {
            binding.streamNameEditText.background =
                ResourcesCompat.getDrawable(resources, R.drawable.bg_edit_text, null)
            binding.warningTextView.text = ""
        }
        binding.streamNameEditText.onFocusChangeListener =
            View.OnFocusChangeListener { _, _ ->
                binding.streamNameEditText.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.bg_edit_text, null)
                binding.warningTextView.text = ""
            }
        binding.streamNameEditText.background =
            ResourcesCompat.getDrawable(resources, R.drawable.bg_edit_text, null)
        binding.warningTextView.text = ""
        binding.createStreamButton.setOnClickListener { createStream() }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.AlertDialogTheme)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView = View.inflate(context, R.layout.create_new_channel_dialog, null)
        dialog.setContentView(contentView)
    }

    private fun createStream() {
        val streamName = binding.streamNameEditText.text.toString()
        val streamDescription = binding.streamDescriptionEditText.text.toString()
        if (streamName.isBlank()) {
            showError()
        } else {
            setFragmentResult(
                resultKey, bundleOf(
                    "streamName" to streamName,
                    "streamDescription" to streamDescription
                )
            )
            dismiss()
        }
    }

    private fun showError() {
        binding.warningTextView.text = getString(R.string.create_stream_without_name_warning)
        binding.streamNameEditText.background =
            ResourcesCompat.getDrawable(resources, R.drawable.bg_error_edit_text, null)
        Toast.makeText(
            context,
            getString(R.string.create_stream_without_name_warning),
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {

        private const val ARG_RESULT_KEY = "RES_KEY"

        fun newInstance(resKey: String): CreateNewChannelDialog {
            return CreateNewChannelDialog().apply {
                arguments = bundleOf(
                    ARG_RESULT_KEY to resKey
                )
            }
        }
    }
}