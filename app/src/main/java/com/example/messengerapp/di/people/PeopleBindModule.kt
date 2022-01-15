package com.example.messengerapp.di.people

import com.example.messengerapp.data.data_provider.PeopleDataProvider
import com.example.messengerapp.data.data_provider.PeopleDataProviderImpl
import com.example.messengerapp.data.data_store.PeopleDataStore
import com.example.messengerapp.data.data_store.PeopleDataStoreImpl
import com.example.messengerapp.data.repository.PeopleRepositoryImpl
import com.example.messengerapp.domain.repository.PeopleRepository
import com.example.messengerapp.domain.usecase.people.SearchPeopleUseCase
import com.example.messengerapp.domain.usecase.people.SearchPeopleUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface PeopleBindModule {

    @Binds
    fun bindPeopleDataProviderImpl_to_PeopleDataProvider(
        peopleDataProviderImpl: PeopleDataProviderImpl
    ): PeopleDataProvider

    @Binds
    fun bindPeopleDataStoreImpl_to_PeopleDataStore(
        peopleDataStoreImpl: PeopleDataStoreImpl
    ): PeopleDataStore

    @Binds
    fun bindSearchPeopleUseCaseImpl_to_SearchPeopleUseCase(
        searchPeopleUseCaseImpl: SearchPeopleUseCaseImpl
    ): SearchPeopleUseCase

    @Binds
    fun bindPeopleRepositoryImpl_to_PeopleRepository(
        peopleRepositoryImpl: PeopleRepositoryImpl
    ): PeopleRepository
}