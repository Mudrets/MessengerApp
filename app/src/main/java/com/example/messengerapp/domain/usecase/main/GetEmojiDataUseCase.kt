package com.example.messengerapp.domain.usecase.main

import com.example.messengerapp.domain.repository.EmojiRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

interface GetEmojiDataUseCase : () -> Observable<Map<String, String>>

class GetEmojiDataUseCaseImpl @Inject constructor(
    private val emojiRepository: EmojiRepository
) : GetEmojiDataUseCase {

    override fun invoke(): Observable<Map<String, String>> = emojiRepository.getEmojiData()

}