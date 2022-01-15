package com.example.messengerapp.presentation.view_group.message

import android.os.Parcelable
import com.example.messengerapp.EmojiInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class EmojiUi(
    val emojiCode: String,
    val emojiName: String,
    val count: Int = 0,
    val isSelected: Boolean = false
) : Parcelable {

    fun select(): EmojiUi {
        return if (!isSelected)
            copy(count = count + 1, isSelected = true)
        else
            this
    }

    fun removeSelection(): EmojiUi {
        return if (isSelected)
            copy(count = count - 1, isSelected = false)
        else
            this
    }

    companion object {

        var emojiInfo: EmojiInfo? = null

        fun firstEmoji(emojiName: String): EmojiUi {
            return EmojiUi(
                emojiCode = emojiInfo?.getEmojiCode(emojiName) ?: "",
                emojiName = emojiName,
                count = 1,
                isSelected = true
            )
        }

    }

}