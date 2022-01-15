package com.example.messengerapp.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.messengerapp.R
import com.example.messengerapp.databinding.ErrorDialogBinding
import com.example.messengerapp.util.ext.fastLazy

class ErrorDialog : DialogFragment() {

    private val title by fastLazy {
        requireArguments().getString(ERROR_TITLE_KEY)
    }
    private val text by fastLazy {
        requireArguments().getString(ERROR_TEXT_KEY)
    }

    private var _binding: ErrorDialogBinding? = null
    val binding: ErrorDialogBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ErrorDialogBinding.inflate(inflater, container, false)

        binding.errorDialogTitle.text = title
        binding.errorDialogMessage.text = text

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.AlertDialogTheme)
    }

    companion object {

        private const val ERROR_TITLE_KEY = "ERROR_TITLE"
        private const val ERROR_TEXT_KEY = "ERROR_TEXT"

        fun newInstance(title: String, text: String): ErrorDialog {
            return ErrorDialog().apply {
                arguments = bundleOf(
                    ERROR_TITLE_KEY to title,
                    ERROR_TEXT_KEY to text
                )
            }
        }

    }

}