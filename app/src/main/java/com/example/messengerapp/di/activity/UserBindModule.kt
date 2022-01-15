package com.example.messengerapp.di.activity

import com.example.messengerapp.data.data_provider.UserDataProvider
import com.example.messengerapp.data.data_provider.UserDataProviderImpl
import com.example.messengerapp.data.data_store.UserDataStore
import com.example.messengerapp.data.data_store.UserDataStoreImpl
import com.example.messengerapp.data.repository.UserRepositoryImpl
import com.example.messengerapp.di.UserMappersBindModule
import com.example.messengerapp.domain.repository.UserRepository
import com.example.messengerapp.domain.usecase.main.GetAuthorizedUserUseCase
import com.example.messengerapp.domain.usecase.main.GetAuthorizedUserUseCaseImpl
import com.example.messengerapp.domain.usecase.people.GetUserStatusUseCase
import com.example.messengerapp.domain.usecase.people.GetUserStatusUseCaseImpl
import dagger.Binds
import dagger.Module

@Module(includes = [UserMappersBindModule::class])
interface UserBindModule {

    @Binds
    fun bindGetAuthorizedUserUseCaseImpl_to_GetAuthorizedUserUseCase(
        getAuthorizedUserUseCaseImpl: GetAuthorizedUserUseCaseImpl
    ): GetAuthorizedUserUseCase

    @Binds
    fun bindGetUserStatusUseCaseImpl_to_GetUserStatusUseCase(
        getUserStatusUseCaseImpl: GetUserStatusUseCaseImpl
    ): GetUserStatusUseCase

    @Binds
    fun bindUserRepositoryImpl_to_UserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    fun bindUserDataStoreImpl_to_UserDataStore(
        userDataStoreImpl: UserDataStoreImpl
    ): UserDataStore

    @Binds
    fun bindUserDataProviderImpl_to_UserDataProvider(
        userDataProviderImpl: UserDataProviderImpl
    ): UserDataProvider
}