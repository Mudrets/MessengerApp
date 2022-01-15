package com.example.messengerapp.util.ext

import timber.log.Timber

fun String?.convertStringToEmojiCode(): String {
    return if (this?.contains('-') == true) {
        val (first, second) = this.split('-')
        first.convertStringToEmojiCode() + "FE0F".convertStringToEmojiCode() + second.convertStringToEmojiCode()
    } else {
        try {
            String(Character.toChars(this?.toInt(radix = 16) ?: 0))
        } catch (ex: Exception) {
            Timber.e(ex)
            ""
        }
    }
}