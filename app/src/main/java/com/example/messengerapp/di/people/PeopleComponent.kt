package com.example.messengerapp.di.people

import com.example.messengerapp.di.AppComponent
import com.example.messengerapp.di.annotation.scope.PeopleFragmentScope
import com.example.messengerapp.presentation.fragment.PeopleFragment
import dagger.Component

@PeopleFragmentScope
@Component(modules = [PeopleModule::class], dependencies = [AppComponent::class])
interface PeopleComponent {

    fun inject(fragment: PeopleFragment)

    @Component.Builder
    interface Builder {
        fun appComponent(component: AppComponent): Builder

        fun build(): PeopleComponent
    }
}