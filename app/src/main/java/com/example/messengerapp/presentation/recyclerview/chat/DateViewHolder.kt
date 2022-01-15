package com.example.messengerapp.presentation.recyclerview.chat

import android.view.View
import android.widget.TextView
import com.example.messengerapp.R
import com.example.messengerapp.presentation.recyclerview.base.BaseViewHolder
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped

data class DateUi(
    val date: String,
    override val uid: String = date,
    override val viewType: Int = R.layout.date_item
) : ViewTyped

class DateViewHolder(view: View) : BaseViewHolder<DateUi>(view) {

    private val dateTextView: TextView = view.findViewById(R.id.date)

    override fun bind(item: DateUi) {
        dateTextView.text = item.date
    }
}