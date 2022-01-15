package com.example.messengerapp.di.channels

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.example.messengerapp.di.annotation.qualifier.AllStreamsRes
import com.example.messengerapp.di.annotation.qualifier.SubscribedStreamsRes
import com.example.messengerapp.di.annotation.scope.ChannelsFragmentScope
import com.example.messengerapp.presentation.dialog.manager.ChannelsDialogManager
import com.example.messengerapp.presentation.elm.channel.ChannelStoreFactory
import com.example.messengerapp.presentation.fragment.StreamsFragment
import com.example.messengerapp.presentation.viewpager.base.PagerAdapter
import com.example.messengerapp.presentation.viewpager.stream.StreamPageType
import com.example.messengerapp.util.Constants
import dagger.Module
import dagger.Provides

@Module(includes = [ChannelsBindModule::class])
class ChannelsModule {

    @Provides
    fun providePagerAdapter(
        lifecycle: Lifecycle,
        fragmentManager: FragmentManager,
        @SubscribedStreamsRes subsKey: String,
        @AllStreamsRes allKey: String
    ) = PagerAdapter(fragmentManager, lifecycle).apply {
        fragments = Constants.STREAM_PAGES.map { pageInfo ->
            when (pageInfo.pageSection) {
                StreamPageType.SUBSCRIBED ->
                    StreamsFragment.newInstance(subsKey, true)
                StreamPageType.ALL_STREAMS ->
                    StreamsFragment.newInstance(allKey, false)
            }
        }
    }

    @ChannelsFragmentScope
    @Provides
    fun provideStore(
        storeFactory: ChannelStoreFactory
    ) = storeFactory.provide()

    @ChannelsFragmentScope
    @Provides
    fun provideChannelsDialogManager(
        context: Context,
        fragmentManager: FragmentManager
    ): ChannelsDialogManager =
        ChannelsDialogManager(context, fragmentManager, CHANNELS_DIALOG_MANAGER_TAG)

    companion object {
        private const val CHANNELS_DIALOG_MANAGER_TAG = "ChannelsDialogManagerTag"
    }
}