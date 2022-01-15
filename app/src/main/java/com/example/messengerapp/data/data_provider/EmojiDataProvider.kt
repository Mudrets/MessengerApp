package com.example.messengerapp.data.data_provider

import io.reactivex.rxjava3.core.Single

interface EmojiDataProvider {
    fun getEmojiData(): Single<Map<String, String>>
}