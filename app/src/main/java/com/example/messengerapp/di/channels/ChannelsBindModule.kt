package com.example.messengerapp.di.channels

import com.example.messengerapp.presentation.dialog.command.ChannelsDialogType
import com.example.messengerapp.presentation.dialog.manager.ChannelsDialogManager
import com.example.messengerapp.presentation.dialog.manager.DialogManager
import dagger.Binds
import dagger.Module

@Module
interface ChannelsBindModule {

    @Binds
    fun bindChannelsDialogManager_to_DialogManager(
        channelsDialogManager: ChannelsDialogManager
    ): DialogManager<ChannelsDialogType>

}