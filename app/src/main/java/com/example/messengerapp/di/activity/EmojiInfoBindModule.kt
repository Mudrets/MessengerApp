package com.example.messengerapp.di.activity

import com.example.messengerapp.data.data_provider.EmojiDataProvider
import com.example.messengerapp.data.data_provider.EmojiDataProviderImpl
import com.example.messengerapp.data.data_store.EmojiDataStore
import com.example.messengerapp.data.data_store.EmojiDataStoreImpl
import com.example.messengerapp.data.repository.EmojiRepositoryImpl
import com.example.messengerapp.domain.repository.EmojiRepository
import com.example.messengerapp.domain.usecase.main.GetEmojiDataUseCase
import com.example.messengerapp.domain.usecase.main.GetEmojiDataUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface EmojiInfoBindModule {

    @Binds
    fun bindEmojiRepositoryImpl_to_EmojiRepository(
        emojiRepositoryImpl: EmojiRepositoryImpl
    ): EmojiRepository

    @Binds
    fun bindGetEmojiDataImpl_to_GetEmojiData(
        getEmojiDataImpl: GetEmojiDataUseCaseImpl
    ): GetEmojiDataUseCase

    @Binds
    fun bindEmojiDataProviderImpl_to_EmojiDataProvider(
        emojiDataProviderImpl: EmojiDataProviderImpl
    ): EmojiDataProvider

    @Binds
    fun bindEmojiDataStoreImpl_to_EmojiDataStore(
        emojiDataStoreImpl: EmojiDataStoreImpl
    ): EmojiDataStore
}