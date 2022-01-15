package com.example.messengerapp.di.chat

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.EmojiInfo
import com.example.messengerapp.di.annotation.qualifier.StreamId
import com.example.messengerapp.di.annotation.qualifier.TopicName
import com.example.messengerapp.di.annotation.scope.ChatFragmentScope
import com.example.messengerapp.presentation.dialog.manager.ChatDialogManager
import com.example.messengerapp.presentation.elm.chat.ChatStoreFactory
import com.example.messengerapp.presentation.models.ChangeMessageReactionRequest
import com.example.messengerapp.presentation.recyclerview.chat.ChatAsyncAdapter
import com.example.messengerapp.presentation.recyclerview.chat.ChatDiffUtilCallback
import com.example.messengerapp.presentation.recyclerview.chat.ChatHolderFactory
import com.example.messengerapp.presentation.recyclerview.chat.MessageUi
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject

@Module(includes = [ChatBindModule::class])
class ChatModule {

    @ChatFragmentScope
    @Provides
    fun provideSelectMessageActionSubjectSubject(): PublishSubject<MessageUi> =
        PublishSubject.create()

    @ChatFragmentScope
    @Provides
    fun provideSelectReactionSubject(): PublishSubject<Long> = PublishSubject.create()

    @ChatFragmentScope
    @Provides
    fun provideChangeReactionSubject(): PublishSubject<ChangeMessageReactionRequest> =
        PublishSubject.create()

    @ChatFragmentScope
    @Provides
    fun provideAdapter(
        holderFactory: ChatHolderFactory,
        diffUtilCallback: ChatDiffUtilCallback
    ): ChatAsyncAdapter = ChatAsyncAdapter(holderFactory, diffUtilCallback)

    @ChatFragmentScope
    @Provides
    fun provideTopicClickSubject(): PublishSubject<String> = PublishSubject.create()

    @ChatFragmentScope
    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()

    @ChatFragmentScope
    @Provides
    fun provideLinearLayoutManager(context: Context): RecyclerView.LayoutManager =
        LinearLayoutManager(context, RecyclerView.VERTICAL, true)

    @ChatFragmentScope
    @Provides
    fun provideStore(
        storeFactory: ChatStoreFactory,
        @TopicName topicName: String,
        @StreamId streamId: Long
    ) = storeFactory.provide(streamId, topicName)

    @ChatFragmentScope
    @Provides
    fun provideDialogManager(
        context: Context,
        fragmentManager: FragmentManager,
        emojiInfo: EmojiInfo
    ): ChatDialogManager =
        ChatDialogManager(context, fragmentManager, emojiInfo, CHAT_DIALOG_MANAGER_TAG)


    companion object {
        private const val CHAT_DIALOG_MANAGER_TAG = "ChatDialogManagerTag"
    }
}