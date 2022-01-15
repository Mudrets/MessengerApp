package com.example.messengerapp

import com.example.messengerapp.domain.usecase.main.GetEmojiDataUseCase
import com.example.messengerapp.presentation.view_group.message.EmojiUi
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmojiInfo @Inject constructor(
    private val getEmojiDataUseCase: GetEmojiDataUseCase
) {

    private var emojiNameToEmojiCode: Map<String, String>? = null

    fun getEmojiCode(emojiName: String): String = emojiNameToEmojiCode?.get(emojiName) ?: ""

    fun getNames(): List<String> = emojiNameToEmojiCode?.keys?.toList() ?: listOf()

    fun getEmojiList(): List<EmojiUi> {
        val list = mutableListOf<EmojiUi>()
        emojiNameToEmojiCode?.forEach { (emojiName, emojiCode) ->
            list.add(
                EmojiUi(
                    emojiCode = emojiCode,
                    emojiName = emojiName
                )
            )
        }
        return list.distinctBy { it.emojiCode }
    }

    fun getEmojiData(): Observable<Map<String, String>> = getEmojiDataUseCase()
        .doOnNext {
            emojiNameToEmojiCode = it
        }

}