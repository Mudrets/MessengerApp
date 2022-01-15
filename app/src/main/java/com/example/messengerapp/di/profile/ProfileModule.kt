package com.example.messengerapp.di.profile

import com.example.messengerapp.AuthorizedUser
import com.example.messengerapp.di.activity.UserBindModule
import com.example.messengerapp.di.annotation.scope.ProfileScope
import com.example.messengerapp.presentation.elm.profile.ProfileStoreFactory
import dagger.Module
import dagger.Provides

@Module(includes = [UserBindModule::class])
class ProfileModule {

    @ProfileScope
    @Provides
    fun provideStore(storeFactory: ProfileStoreFactory) = storeFactory.provide()

    @Provides
    fun provideAuthorizeUserUi(authorizedUser: AuthorizedUser) = authorizedUser.userUi
}