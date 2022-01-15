package com.example.messengerapp.di

import android.content.Context
import com.example.messengerapp.App
import com.example.messengerapp.AuthorizedUser
import com.example.messengerapp.EmojiInfo
import com.example.messengerapp.presentation.elm.channel.ChannelStoreFactory
import com.example.messengerapp.presentation.elm.chat.ChatStoreFactory
import com.example.messengerapp.presentation.elm.main.MainStoreFactory
import com.example.messengerapp.presentation.elm.people.PeopleStoreFactory
import com.example.messengerapp.presentation.elm.profile.ProfileStoreFactory
import com.example.messengerapp.presentation.elm.stream.StreamStoreFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(app: App)

    val chatStoreFactory: ChatStoreFactory
    val streamStoreFactory: StreamStoreFactory
    val profileStoreFactory: ProfileStoreFactory
    val peopleStoreFactory: PeopleStoreFactory
    val mainStoreFactory: MainStoreFactory
    val channelStoreFactory: ChannelStoreFactory
    val authorizedUser: AuthorizedUser
    val emojiInfo: EmojiInfo
    val context: Context

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }
}