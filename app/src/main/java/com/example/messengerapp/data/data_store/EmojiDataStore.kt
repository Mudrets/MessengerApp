package com.example.messengerapp.data.data_store

import com.example.messengerapp.data.data_provider.EmojiDataProvider

interface EmojiDataStore : EmojiDataProvider {
    fun insertEmojiData(emojiMap: Map<String, String>)
}