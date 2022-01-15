package com.example.messengerapp.di.streams

import com.example.messengerapp.di.AppComponent
import com.example.messengerapp.di.annotation.scope.StreamsFragmentScope
import com.example.messengerapp.presentation.fragment.StreamsFragment
import dagger.BindsInstance
import dagger.Component

@StreamsFragmentScope
@Component(modules = [StreamsModule::class], dependencies = [AppComponent::class])
interface StreamsComponent {

    fun inject(fragment: StreamsFragment)

    @Component.Builder
    interface Builder {

        fun appComponent(component: AppComponent): Builder

        @BindsInstance
        fun isSubscribed(isSubscribed: Boolean): Builder

        fun build(): StreamsComponent
    }
}