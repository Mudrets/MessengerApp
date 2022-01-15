package com.example.messengerapp.di.streams

import com.example.messengerapp.data.db.mapper.from_db.TopicDbToTopicMapper
import com.example.messengerapp.data.db.mapper.from_db.TopicDbToTopicMapperImpl
import com.example.messengerapp.data.db.mapper.to_db.TopicToTopicDbMapper
import com.example.messengerapp.data.db.mapper.to_db.TopicToTopicDbMapperImpl
import com.example.messengerapp.presentation.mapper.TopicToTopicUiMapper
import com.example.messengerapp.presentation.mapper.TopicToTopicUiMapperImpl
import dagger.Binds
import dagger.Module

@Module
interface TopicBindModule {

    @Binds
    fun bindTopicToTopicUiMapperImpl_to_TopicToTopicUiMapper(
        topicToTopicUiMapperImpl: TopicToTopicUiMapperImpl
    ): TopicToTopicUiMapper

    @Binds
    fun bindTopicDbToTopicMapperImpl_to_TopicDbToTopicMapper(
        topicDbToTopicMapperImpl: TopicDbToTopicMapperImpl
    ): TopicDbToTopicMapper

    @Binds
    fun bindTopicToTopicDbMapperImpl_to_TopicToTopicDbMapper(
        topicToTopicDbMapperImpl: TopicToTopicDbMapperImpl
    ): TopicToTopicDbMapper
}