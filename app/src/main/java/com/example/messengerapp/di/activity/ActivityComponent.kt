package com.example.messengerapp.di.activity

import com.example.messengerapp.MainActivity
import com.example.messengerapp.di.AppComponent
import com.example.messengerapp.di.annotation.scope.ActivityScope
import dagger.Component

@ActivityScope
@Component(modules = [ActivityModule::class], dependencies = [AppComponent::class])
interface ActivityComponent {
    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder {
        fun appComponent(appComponent: AppComponent): Builder
        fun build(): ActivityComponent
    }
}