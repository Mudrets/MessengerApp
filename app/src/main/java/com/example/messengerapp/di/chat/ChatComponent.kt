package com.example.messengerapp.di.chat

import androidx.fragment.app.FragmentManager
import com.example.messengerapp.di.AppComponent
import com.example.messengerapp.di.annotation.qualifier.StreamId
import com.example.messengerapp.di.annotation.qualifier.TopicName
import com.example.messengerapp.di.annotation.scope.ChatFragmentScope
import com.example.messengerapp.presentation.fragment.chat.ChatFragment
import dagger.BindsInstance
import dagger.Component

@ChatFragmentScope
@Component(modules = [ChatModule::class], dependencies = [AppComponent::class])
interface ChatComponent {

    fun inject(fragment: ChatFragment)

    @Component.Builder
    interface Builder {

        fun appComponent(component: AppComponent): Builder

        @BindsInstance
        fun fragmentManager(fragmentManager: FragmentManager): Builder

        @BindsInstance
        fun topicName(@TopicName topicName: String): Builder

        @BindsInstance
        fun streamId(@StreamId streamId: Long): Builder

        fun build(): ChatComponent
    }
}