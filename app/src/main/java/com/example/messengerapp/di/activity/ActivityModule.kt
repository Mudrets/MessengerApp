package com.example.messengerapp.di.activity

import com.example.messengerapp.di.chat.ChatBindModule
import com.example.messengerapp.di.people.PeopleBindModule
import com.example.messengerapp.di.streams.StreamsBindModule
import dagger.Module

@Module(includes = [UserBindModule::class, PeopleBindModule::class, StreamsBindModule::class, ChatBindModule::class])
class ActivityModule