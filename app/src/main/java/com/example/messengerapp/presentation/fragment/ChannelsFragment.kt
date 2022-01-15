package com.example.messengerapp.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.messengerapp.R
import com.example.messengerapp.databinding.FragmentChannelsBinding
import com.example.messengerapp.di.channels.ChannelsComponent
import com.example.messengerapp.di.channels.DaggerChannelsComponent
import com.example.messengerapp.presentation.dialog.command.ChannelsDialogType
import com.example.messengerapp.presentation.dialog.command.DialogCommand
import com.example.messengerapp.presentation.dialog.manager.DialogManager
import com.example.messengerapp.presentation.elm.channel.model.ChannelCommand
import com.example.messengerapp.presentation.elm.channel.model.ChannelEffect
import com.example.messengerapp.presentation.elm.channel.model.ChannelEvent
import com.example.messengerapp.presentation.elm.channel.model.ChannelState
import com.example.messengerapp.presentation.viewpager.base.PagerAdapter
import com.example.messengerapp.util.Constants.STREAM_PAGES
import com.example.messengerapp.util.ext.appComponent
import com.example.messengerapp.util.ext.fastLazy
import com.google.android.material.tabs.TabLayoutMediator
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject
import javax.inject.Provider

class ChannelsFragment : ElmFragment<ChannelEvent, ChannelEffect, ChannelState>() {

    private val resultKey by fastLazy { requireArguments().getString(ARG_RESULT_KEY, "")!! }

    private var _binding: FragmentChannelsBinding? = null
    val binding: FragmentChannelsBinding
        get() = _binding!!

    private lateinit var channelsComponent: ChannelsComponent

    @Inject
    lateinit var adapter: Provider<PagerAdapter>

    @Inject
    lateinit var channelStore: ElmStore<ChannelEvent, ChannelState, ChannelEffect, ChannelCommand>

    @Inject
    lateinit var dialogManager: DialogManager<ChannelsDialogType>

    override fun onAttach(context: Context) {
        channelsComponent = DaggerChannelsComponent.builder()
            .appComponent(context.appComponent)
            .lifecycle(lifecycle)
            .fragmentManager(childFragmentManager)
            .subscribedStreamResKey(SUBSCRIBED_STREAMS_RES_KEY)
            .allStreamResKey(ALL_STREAMS_RES_KEY)
            .build()
        channelsComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelsBinding.inflate(layoutInflater, container, false)
        dialogManager.lifecycleOwner = viewLifecycleOwner
        createViewPager()
        setOnEnterListener()

        dialogManager.addListener(ChannelsDialogType.CREATE_STREAM) { bundle ->
            val streamName = bundle.getString("streamName")
            val description = bundle.getString("streamDescription")
            if (streamName != null && description != null)
                store.accept(ChannelEvent.Ui.CreateNewStream(streamName, description))
        }

        dialogManager.setListeners()

        childFragmentManager.setFragmentResultListener(
            ALL_STREAMS_RES_KEY,
            viewLifecycleOwner
        ) { _, bundle -> launchChatFragment(bundle) }

        childFragmentManager.setFragmentResultListener(
            SUBSCRIBED_STREAMS_RES_KEY,
            viewLifecycleOwner
        ) { _, bundle -> launchChatFragment(bundle) }

        binding.createNewStreamButton.setOnClickListener {
            showDialog(ChannelsDialogType.CREATE_STREAM)
        }

        return binding.root
    }

    override fun onDestroyView() {
        dialogManager.lifecycleOwner = null
        super.onDestroyView()
    }

    private fun showDialog(actionType: ChannelsDialogType, data: Any? = null) {
        dialogManager.showDialog(
            DialogCommand(
                type = actionType,
                data = bundleOf(DialogManager.ACTION_DATA_KEY to data)
            )
        )
    }

    private fun launchChatFragment(bundle: Bundle) {
        val streamName = bundle.getString("streamName") ?: ""
        val topicName = bundle.getString("topicName") ?: ""
        val streamId = bundle.getLong("streamId")
        setFragmentResult(
            resultKey, bundleOf(
                "streamName" to streamName,
                "topicName" to topicName,
                "streamId" to streamId
            )
        )
    }

    private fun setOnEnterListener() {
        binding.searchChannelEditText.doAfterTextChanged { editable ->
            val searchQuery = editable.toString()
            StreamsFragment.searchSubject.onNext(searchQuery)
        }
    }

    private fun createViewPager() {
        val tabs = listOf(
            getString(STREAM_PAGES[0].resourceId),
            getString(STREAM_PAGES[1].resourceId),
        )

        binding.apply {
            streamsViewPager.adapter = adapter.get()
            TabLayoutMediator(tabLayoutChannels, streamsViewPager) { tab, position ->
                tab.text = tabs[position]
                streamsViewPager.setCurrentItem(tab.position, true)
            }.attach()
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override val initEvent: ChannelEvent = ChannelEvent.Ui.Init

    override fun createStore(): Store<ChannelEvent, ChannelEffect, ChannelState> = channelStore

    override fun render(state: ChannelState) = when (state) {
        ChannelState.Normal -> {
            binding.createNewStreamButton.isVisible = true
            binding.createStreamProgressBar.isVisible = false
        }
        ChannelState.Loading -> {
            binding.createNewStreamButton.isVisible = false
            binding.createStreamProgressBar.isVisible = true
        }
    }

    override fun handleEffect(effect: ChannelEffect) = when (effect) {
        is ChannelEffect.ShowError -> dialogManager.showError(effect.error, effect.streamName)
        is ChannelEffect.ShowSuccess -> {
            reloadStreams()
            Toast.makeText(
                context,
                getString(R.string.stream_created_successful_message, effect.streamName),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun reloadStreams() {
        val allStreamsFragment =
            childFragmentManager.findFragmentByTag("f0") as? StreamsFragment
        val subsStreamsFragment =
            childFragmentManager.findFragmentByTag("f1") as? StreamsFragment
        allStreamsFragment?.reloadStreams()
        subsStreamsFragment?.reloadStreams()
    }

    companion object {

        private const val ARG_RESULT_KEY = "ARG_RESULT_KEY"
        private const val ALL_STREAMS_RES_KEY = "AllStreamsFragment"
        private const val SUBSCRIBED_STREAMS_RES_KEY = "SubscribedStreamsFragment"

        fun newInstance(resultKey: String): Fragment {
            return ChannelsFragment().apply {
                arguments = bundleOf(
                    ARG_RESULT_KEY to resultKey,
                )
            }
        }
    }

}