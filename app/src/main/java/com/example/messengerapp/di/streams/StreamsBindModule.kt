package com.example.messengerapp.di.streams

import com.example.messengerapp.data.data_provider.StreamsDataProvider
import com.example.messengerapp.data.data_provider.StreamsDataProviderImpl
import com.example.messengerapp.data.data_store.StreamsDataStore
import com.example.messengerapp.data.data_store.StreamsDataStoreImpl
import com.example.messengerapp.data.db.mapper.from_db.StreamPojoToStreamMapper
import com.example.messengerapp.data.db.mapper.from_db.StreamPojoToStreamMapperImpl
import com.example.messengerapp.data.db.mapper.to_db.StreamToStreamPageDbMapper
import com.example.messengerapp.data.db.mapper.to_db.StreamToStreamPageDbMapperImpl
import com.example.messengerapp.data.db.mapper.to_db.StreamToStreamPojoMapper
import com.example.messengerapp.data.db.mapper.to_db.StreamToStreamPojoMapperImpl
import com.example.messengerapp.data.network.executor.ChannelExecutor
import com.example.messengerapp.data.network.executor.ChannelExecutorImpl
import com.example.messengerapp.data.network.mapper.NetworkStreamToStreamMapper
import com.example.messengerapp.data.network.mapper.NetworkStreamToStreamMapperImpl
import com.example.messengerapp.data.network.mapper.NetworkSubscriptionToStreamMapper
import com.example.messengerapp.data.network.mapper.NetworkSubscriptionToStreamMapperImpl
import com.example.messengerapp.data.repository.ChannelRepositoryImpl
import com.example.messengerapp.domain.repository.ChannelRepository
import com.example.messengerapp.domain.usecase.channel.CreateNewStreamUseCase
import com.example.messengerapp.domain.usecase.channel.CreateNewStreamUseCaseImpl
import com.example.messengerapp.domain.usecase.channel.SearchStreamsUseCase
import com.example.messengerapp.domain.usecase.channel.SearchStreamsUseCaseImpl
import com.example.messengerapp.presentation.mapper.StreamToStreamUiMapper
import com.example.messengerapp.presentation.mapper.StreamToStreamUiMapperImpl
import dagger.Binds
import dagger.Module

@Module(includes = [TopicBindModule::class])
interface StreamsBindModule {

    @Binds
    fun bindStreamsDataProviderImpl_to_StreamsDataProvider(
        streamsDataProviderImpl: StreamsDataProviderImpl
    ): StreamsDataProvider

    @Binds
    fun bindStreamsDataStoreImpl_to_StreamsDataStore(
        streamsDataStoreImpl: StreamsDataStoreImpl
    ): StreamsDataStore

    @Binds
    fun bindNetworkStreamToStreamMapperImpl_to_NetworkStreamToStreamMapper(
        networkStreamToStreamMapperImpl: NetworkStreamToStreamMapperImpl
    ): NetworkStreamToStreamMapper

    @Binds
    fun bindStreamToStreamUiMapperImpl_to_StreamToStreamUiMapper(
        streamToStreamUiMapperImpl: StreamToStreamUiMapperImpl
    ): StreamToStreamUiMapper

    @Binds
    fun bindSearchStreamsUseCaseImpl_to_SearchStreamsUseCase(
        searchStreamsUseCaseImpl: SearchStreamsUseCaseImpl
    ): SearchStreamsUseCase

    @Binds
    fun bindNetworkSubscriptionToStreamMapperImpl_to_NetworkSubscriptionToStreamMapper(
        networkSubscriptionToStreamMapperImpl: NetworkSubscriptionToStreamMapperImpl
    ): NetworkSubscriptionToStreamMapper

    @Binds
    fun bindStreamPojoToStreamMapperImpl_to_StreamPojoToStreamMapper(
        streamPojoToStreamMapperImpl: StreamPojoToStreamMapperImpl
    ): StreamPojoToStreamMapper

    @Binds
    fun bindStreamToStreamPageDbMapperImpl_to_StreamToStreamPageDbMapper(
        streamToStreamPageDbMapperImpl: StreamToStreamPageDbMapperImpl
    ): StreamToStreamPageDbMapper

    @Binds
    fun bindStreamToStreamPojoMapperImpl_to_StreamToStreamPojoMapper(
        streamToStreamPojoMapperImpl: StreamToStreamPojoMapperImpl
    ): StreamToStreamPojoMapper

    @Binds
    fun bindChannelRepositoryImpl_to_ChannelRepository(
        channelRepositoryImpl: ChannelRepositoryImpl
    ): ChannelRepository

    @Binds
    fun bindChannelExecutorImpl_to_ChannelExecutor(
        channelExecutorImpl: ChannelExecutorImpl
    ): ChannelExecutor

    @Binds
    fun bindCreateNewStreamUseCaseImpl_to_CreateNewStreamUseCase(
        createNewStreamUseCaseImpl: CreateNewStreamUseCaseImpl
    ): CreateNewStreamUseCase
}