package com.example.messengerapp.presentation.elm.chat.model

import android.os.Parcelable
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ChatState(
    val messages: @RawValue List<ViewTyped> = listOf(),
    val isEmptyState: Boolean = true,
    val error: Throwable? = null,
    val isLoading: Boolean = true,
    val pageNumber: Int = INITIAL_PAGE
) : Parcelable {
    companion object {
        const val INITIAL_PAGE = 0
    }
}