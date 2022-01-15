package com.example.messengerapp.presentation.mapper

import com.example.messengerapp.domain.entity.channel.Topic
import com.example.messengerapp.presentation.recyclerview.stream.TopicUi

interface TopicToTopicUiMapper : (Int, Topic, String, Long) -> TopicUi