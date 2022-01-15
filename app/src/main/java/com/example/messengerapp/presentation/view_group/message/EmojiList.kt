package com.example.messengerapp.presentation.view_group.message

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.example.messengerapp.R
import com.example.messengerapp.presentation.view.EmojiView

class EmojiList(
    private val viewGroup: ViewGroup,
    private val context: Context
) {

    private var emojiOnClickListener: (View) -> Unit = { }
    private val emojiList: MutableList<EmojiUi> = mutableListOf()

    fun isEmpty() = emojiList.sumBy { it.count } == 0

    fun set(elements: Collection<EmojiUi>) {
        clear()
        emojiList.addAll(elements)
        for ((i, emoji) in elements.withIndex()) {
            if (i < viewGroup.childCount - 1) {
                val child = viewGroup.getChildAt(i)
                (child as? EmojiView)?.emoji = emoji
            } else {
                val emojiView =
                    EmojiView(ContextThemeWrapper(context, R.style.Message_EmojiReaction))
                emojiView.emoji = emoji
                emojiView.setOnClickListener(emojiOnClickListener)
                viewGroup.addView(emojiView, i)
            }
        }
    }

    private fun clear() {
        emojiList.clear()
        for (i in 0 until viewGroup.childCount - 1) {
            viewGroup.removeViewAt(0)
        }
    }

    fun setEmojiOnClickListener(
        emojiOnClickListener: (EmojiView) -> Unit
    ) {
        this.emojiOnClickListener = { view ->
            if (view is EmojiView) {
                emojiOnClickListener(view)
            }
        }
        for (child in viewGroup.children) {
            (child as? EmojiView)?.setOnClickListener(this.emojiOnClickListener)
        }
    }
}