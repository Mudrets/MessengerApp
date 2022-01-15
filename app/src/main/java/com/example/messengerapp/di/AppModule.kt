package com.example.messengerapp.di

import com.example.messengerapp.AuthorizedUser
import com.example.messengerapp.di.activity.EmojiInfoBindModule
import com.example.messengerapp.di.activity.UserBindModule
import com.example.messengerapp.di.annotation.qualifier.AllStreamsReducer
import com.example.messengerapp.di.annotation.qualifier.StreamChatActor
import com.example.messengerapp.di.annotation.qualifier.StreamChatReducer
import com.example.messengerapp.di.annotation.qualifier.SubsStreamsReducer
import com.example.messengerapp.di.chat.ChatBindModule
import com.example.messengerapp.di.people.PeopleBindModule
import com.example.messengerapp.di.streams.StreamsBindModule
import com.example.messengerapp.domain.usecase.chat.*
import com.example.messengerapp.presentation.elm.chat.ChatActor
import com.example.messengerapp.presentation.elm.chat.ChatReducer
import com.example.messengerapp.presentation.elm.stream.StreamReducer
import com.example.messengerapp.presentation.mapper.MessagesListToViewTypedWithTopicDelimiters
import com.example.messengerapp.presentation.recyclerview.chat.pagination.ChatPaginationHelper
import com.example.messengerapp.presentation.recyclerview.chat.pagination.StreamChatListManager
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module(
    includes = [RoomModule::class, NetworkModule::class, UserBindModule::class,
        PeopleBindModule::class, StreamsBindModule::class, ChatBindModule::class, EmojiInfoBindModule::class]
)
class AppModule {

    @SubsStreamsReducer
    @Provides
    fun provideSubscribedStreamsReducer(): StreamReducer =
        StreamReducer(true)

    @AllStreamsReducer
    @Provides
    fun provideAllStreamsReducer(): StreamReducer =
        StreamReducer(false)

    @Provides
    fun provideAuthorizedUserId(authorizedUser: AuthorizedUser): Long = authorizedUser.userId ?: -1

    @StreamChatActor
    @Provides
    fun provideStreamChatActor(
        sendMessageUseCase: SendMessageUseCase,
        getMessagesUseCase: GetMessagesUseCase,
        addReactionUseCase: AddReactionUseCase,
        removeReactionUseCase: RemoveReactionUseCase,
        deleteMessageUseCase: DeleteMessageUseCase,
        changeTopicUseCase: ChangeTopicUseCase,
        editMessageUseCase: EditMessageUseCase,
        messagesListToViewTypedWithTopicDelimiters: MessagesListToViewTypedWithTopicDelimiters,
        authorizedUserId: Provider<Long>
    ): ChatActor =
        ChatActor(
            sendMessageUseCase = sendMessageUseCase,
            getMessagesUseCase = getMessagesUseCase,
            addReactionUseCase = addReactionUseCase,
            removeReactionUseCase = removeReactionUseCase,
            deleteMessageUseCase = deleteMessageUseCase,
            changeTopicUseCase = changeTopicUseCase,
            editMessageUseCase = editMessageUseCase,
            messagesListToViewTypedListMapper = messagesListToViewTypedWithTopicDelimiters,
            authorizedUserId = authorizedUserId
        )

    @Provides
    fun provideStreamChatList(
        chatPaginationHelper: ChatPaginationHelper,
        messagesListToViewTypedWithTopicDelimiters: MessagesListToViewTypedWithTopicDelimiters
    ): StreamChatListManager =
        StreamChatListManager(chatPaginationHelper, messagesListToViewTypedWithTopicDelimiters)

    @StreamChatReducer
    @Provides
    fun provideStreamChatReducer(
        streamChatList: StreamChatListManager
    ): ChatReducer = ChatReducer(streamChatList)

}