package com.example.messengerapp.di.profile

import com.example.messengerapp.di.AppComponent
import com.example.messengerapp.di.annotation.scope.ProfileScope
import com.example.messengerapp.presentation.fragment.ProfileFragment
import dagger.Component

@ProfileScope
@Component(modules = [ProfileModule::class], dependencies = [AppComponent::class])
interface ProfileComponent {

    fun inject(fragment: ProfileFragment)

    @Component.Builder
    interface Builder {
        fun appComponent(component: AppComponent): Builder
        fun build(): ProfileComponent
    }
}