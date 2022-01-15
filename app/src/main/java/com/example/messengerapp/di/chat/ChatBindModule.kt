package com.example.messengerapp.di.chat

import com.example.messengerapp.data.data_provider.MessagesDataProvider
import com.example.messengerapp.data.data_provider.MessagesDataProviderImpl
import com.example.messengerapp.data.data_store.MessagesDataStore
import com.example.messengerapp.data.data_store.MessagesDataStoreImpl
import com.example.messengerapp.data.db.mapper.from_db.MessagePojoToMessageMapper
import com.example.messengerapp.data.db.mapper.from_db.MessagePojoToMessageMapperImpl
import com.example.messengerapp.data.db.mapper.from_db.ReactionListDbToEmojiListMapper
import com.example.messengerapp.data.db.mapper.from_db.ReactionListDbToEmojiListMapperImpl
import com.example.messengerapp.data.db.mapper.to_db.EmojiListToReactionDbListMapper
import com.example.messengerapp.data.db.mapper.to_db.EmojiListToReactionDbListMapperImpl
import com.example.messengerapp.data.db.mapper.to_db.MessageToMessagePojoMapper
import com.example.messengerapp.data.db.mapper.to_db.MessageToMessagePojoMapperImpl
import com.example.messengerapp.data.network.executor.ChatExecutor
import com.example.messengerapp.data.network.executor.ChatExecutorImpl
import com.example.messengerapp.data.network.mapper.NetworkMessageToMessageMapper
import com.example.messengerapp.data.network.mapper.NetworkMessageToMessageMapperImpl
import com.example.messengerapp.data.repository.ChatRepositoryImpl
import com.example.messengerapp.domain.repository.ChatRepository
import com.example.messengerapp.domain.usecase.chat.*
import com.example.messengerapp.presentation.dialog.command.ChatDialogType
import com.example.messengerapp.presentation.dialog.manager.ChatDialogManager
import com.example.messengerapp.presentation.dialog.manager.DialogManager
import com.example.messengerapp.presentation.mapper.*
import com.example.messengerapp.presentation.recyclerview.chat.pagination.ChatListManager
import com.example.messengerapp.presentation.recyclerview.chat.pagination.TopicChatListManager
import dagger.Binds
import dagger.Module

@Module
interface ChatBindModule {

    @Binds
    fun bindMessagesDataProviderImpl_to_MessagesDataProvider(
        messagesDataProviderImpl: MessagesDataProviderImpl
    ): MessagesDataProvider

    @Binds
    fun bindMessagesDataStoreImpl_to_MessagesDataStore(
        messagesDataStoreImpl: MessagesDataStoreImpl
    ): MessagesDataStore

    @Binds
    fun bindNetworkMessageToMessageMapperImpl_to_NetworkMessageToMessageMapper(
        networkMessageToMessageMapperImpl: NetworkMessageToMessageMapperImpl
    ): NetworkMessageToMessageMapper

    @Binds
    fun bindMessagesListToViewTypedListMapperImpl_to_MessagesListToViewTypedListMapper(
        messagesListToViewTypedListMapperImpl: MessagesListToViewTypedListMapperImpl
    ): MessagesListToViewTypedListMapper

    @Binds
    fun bindMessageToMessageUiMapperImpl_to_MessageToMessageUiMapper(
        messageToMessageUiMapperImpl: MessageToMessageUiMapperImpl
    ): MessageToMessageUiMapper

    @Binds
    fun bindEmojiToEmojiUiMapperImpl_to_EmojiToEmojiUiMapper(
        emojiToEmojiUiMapperImpl: EmojiToEmojiUiMapperImpl
    ): EmojiToEmojiUiMapper

    @Binds
    fun bindAddReactionUseCaseImpl_to_AddReactionUseCase(
        addReactionUseCaseImpl: AddReactionUseCaseImpl
    ): AddReactionUseCase

    @Binds
    fun bindGetMessagesUseCaseImpl_to_GetMessagesUseCase(
        getMessagesUseCaseImpl: GetMessagesUseCaseImpl
    ): GetMessagesUseCase

    @Binds
    fun bindRemoveReactionUseCaseImpl_to_RemoveReactionUseCase(
        removeReactionUseCaseImpl: RemoveReactionUseCaseImpl
    ): RemoveReactionUseCase

    @Binds
    fun bindSendMessageUseCaseImpl_to_SendMessageUseCase(
        sendMessageUseCaseImpl: SendMessageUseCaseImpl
    ): SendMessageUseCase

    @Binds
    fun bindEditMessageUseCaseImpl_to_EditMessageUseCase(
        editMessageUseCaseImpl: EditMessageUseCaseImpl
    ): EditMessageUseCase

    @Binds
    fun bindDeleteMessageUseCaseImpl_to_DeleteMessageUseCase(
        deleteMessageUseCaseImpl: DeleteMessageUseCaseImpl
    ): DeleteMessageUseCase

    @Binds
    fun bindChangeTopicUseCaseImpl_to_ChangeTopicUseCase(
        changeTopicUseCaseImpl: ChangeTopicUseCaseImpl
    ): ChangeTopicUseCase

    @Binds
    fun bindReactionListDbToEmojiListMapperImpl_to_ReactionListDbToEmojiListMapper(
        reactionListDbToEmojiListMapper: ReactionListDbToEmojiListMapperImpl
    ): ReactionListDbToEmojiListMapper

    @Binds
    fun bindEmojiListToReactionDbListMapperImpl_to_EmojiListToReactionDbListMapper(
        emojiListToReactionDbListMapperImpl: EmojiListToReactionDbListMapperImpl
    ): EmojiListToReactionDbListMapper

    @Binds
    fun bindMessageToMessagePojoMapperImpl_to_MessageToMessagePojoMapper(
        messageToMessagePojoMapperImpl: MessageToMessagePojoMapperImpl
    ): MessageToMessagePojoMapper

    @Binds
    fun bindMessagePojoToMessageMapperImpl_to_MessagePojoToMessageMapper(
        messagePojoToMessageMapperImpl: MessagePojoToMessageMapperImpl
    ): MessagePojoToMessageMapper

    @Binds
    fun bindChatRepositoryImpl_to_ChatRepository(
        chatRepositoryImpl: ChatRepositoryImpl
    ): ChatRepository

    @Binds
    fun bindChatExecutorImpl_to_ChatExecutor(
        chatExecutorImpl: ChatExecutorImpl
    ): ChatExecutor

    @Binds
    fun bindTopicChat_to_ChatList(
        topicChatList: TopicChatListManager
    ): ChatListManager

    @Binds
    fun bindChatDialogManager_to_DialogManager(
        chatDialogManager: ChatDialogManager
    ): DialogManager<ChatDialogType>
}