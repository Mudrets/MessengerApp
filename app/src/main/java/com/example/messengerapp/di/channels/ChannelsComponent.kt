package com.example.messengerapp.di.channels

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.example.messengerapp.di.AppComponent
import com.example.messengerapp.di.annotation.qualifier.AllStreamsRes
import com.example.messengerapp.di.annotation.qualifier.SubscribedStreamsRes
import com.example.messengerapp.di.annotation.scope.ChannelsFragmentScope
import com.example.messengerapp.presentation.fragment.ChannelsFragment
import dagger.BindsInstance
import dagger.Component

@ChannelsFragmentScope
@Component(modules = [ChannelsModule::class], dependencies = [AppComponent::class])
interface ChannelsComponent {
    fun inject(fragment: ChannelsFragment)

    @Component.Builder
    interface Builder {

        fun appComponent(appComponent: AppComponent): Builder

        @BindsInstance
        fun lifecycle(lifecycle: Lifecycle): Builder

        @BindsInstance
        fun fragmentManager(fragmentManager: FragmentManager): Builder

        @BindsInstance
        fun subscribedStreamResKey(@SubscribedStreamsRes key: String): Builder

        @BindsInstance
        fun allStreamResKey(@AllStreamsRes key: String): Builder
        fun build(): ChannelsComponent
    }
}