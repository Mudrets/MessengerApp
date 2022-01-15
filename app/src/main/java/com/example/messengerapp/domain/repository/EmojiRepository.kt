package com.example.messengerapp.domain.repository

import io.reactivex.rxjava3.core.Observable

interface EmojiRepository {

    fun getEmojiData(): Observable<Map<String, String>>

}