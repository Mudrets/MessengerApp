package com.example.messengerapp.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.databinding.FragmentStreamsBinding
import com.example.messengerapp.di.annotation.qualifier.DividerItemDecorator
import com.example.messengerapp.di.streams.DaggerStreamsComponent
import com.example.messengerapp.di.streams.StreamsComponent
import com.example.messengerapp.presentation.elm.stream.model.StreamCommand
import com.example.messengerapp.presentation.elm.stream.model.StreamEffect
import com.example.messengerapp.presentation.elm.stream.model.StreamEvent
import com.example.messengerapp.presentation.elm.stream.model.StreamState
import com.example.messengerapp.presentation.recyclerview.base.AsyncAdapter
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped
import com.example.messengerapp.presentation.recyclerview.stream.StreamShimmer
import com.example.messengerapp.presentation.recyclerview.stream.StreamUi
import com.example.messengerapp.util.Constants.INITIAL_QUERY
import com.example.messengerapp.util.ext.appComponent
import com.example.messengerapp.util.ext.fastLazy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class StreamsFragment : ElmFragment<StreamEvent, StreamEffect, StreamState>() {

    private val resultKey by fastLazy { requireArguments().getString(ARG_RESULT_KEY, "")!! }
    private val isSubscribed by fastLazy { requireArguments().getBoolean(IS_SUBSCRIBED_STREAMS) }

    private var _binding: FragmentStreamsBinding? = null
    val binding: FragmentStreamsBinding
        get() = _binding!!

    private lateinit var streamsComponent: StreamsComponent

    private var lastSearchQuery = INITIAL_QUERY

    @Inject
    lateinit var streamsStore: ElmStore<StreamEvent, StreamState, StreamEffect, StreamCommand>

    @Inject
    lateinit var adapter: AsyncAdapter<ViewTyped>

    @Inject
    lateinit var layoutManager: Provider<RecyclerView.LayoutManager>

    @Inject
    lateinit var openChatSubject: PublishSubject<Triple<String, Long, String>>

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    @DividerItemDecorator
    @Inject
    lateinit var dividerItemDecoration: RecyclerView.ItemDecoration

    override fun onAttach(context: Context) {
        streamsComponent = DaggerStreamsComponent.builder()
            .appComponent(context.appComponent)
            .isSubscribed(isSubscribed)
            .build()
        streamsComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchSubject
            .distinctUntilChanged()
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .subscribeBy { searchQuery ->
                lastSearchQuery = searchQuery
                store.accept(StreamEvent.Ui.SearchStreams(searchQuery, isSubscribed))
            }.addTo(compositeDisposable)

        openChatSubject.subscribeBy { (streamName, streamId, topicName) ->
            store.accept(StreamEvent.Ui.OpenChat(streamName, streamId, topicName))
        }.addTo(compositeDisposable)

        if (savedInstanceState == null)
            store.accept(StreamEvent.Ui.SearchStreams(INITIAL_QUERY, isSubscribed))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStreamsBinding.inflate(layoutInflater, container, false)
        initUi()

        return binding.root
    }

    private fun initUi() {
        binding.streamsRecyclerView.adapter = adapter
        binding.streamsRecyclerView.layoutManager = layoutManager.get()
        binding.reloadStreamsButton.setOnClickListener {
            store.accept(StreamEvent.Ui.SearchStreams(lastSearchQuery, isSubscribed))
        }
    }

    private fun normalState() {
        binding.apply {
            streamsRecyclerView.isVisible = true
            streamErrorText.isVisible = false
            errorStreamImage.isVisible = false
            reloadStreamsButton.isVisible = false
            emptyStreamListImage.isVisible = false
            emptyStreamListText.isVisible = false
        }
    }

    private fun showEmptyListWarning() {
        binding.apply {
            streamsRecyclerView.isVisible = false
            emptyStreamListImage.isVisible = true
            emptyStreamListText.isVisible = true
        }
    }

    private fun showError() {
        binding.apply {
            streamsRecyclerView.isVisible = false
            streamErrorText.isVisible = true
            errorStreamImage.isVisible = true
            reloadStreamsButton.isVisible = true
        }
    }

    private fun showLoad() {
        normalState()
        binding.streamsRecyclerView.removeItemDecoration(dividerItemDecoration)
        adapter.items =
            generateSequence { StreamShimmer() }.take(COUNT_SHIMMERS_FOR_LOADING).toList()
    }

    override val initEvent: StreamEvent = StreamEvent.Ui.Init

    override fun createStore(): Store<StreamEvent, StreamEffect, StreamState> = streamsStore

    override fun render(state: StreamState) = when (state) {
        is StreamState.Error -> {
            if (adapter.items.indexOfFirst { it is StreamUi } == -1)
                showError()
            else
                Unit
        }
        StreamState.Loading -> showLoad()
        is StreamState.Success -> {
            normalState()
            if (state.streams.isNotEmpty()) {
                if (binding.streamsRecyclerView.itemDecorationCount == 0)
                    binding.streamsRecyclerView.addItemDecoration(dividerItemDecoration)
            } else {
                showEmptyListWarning()
            }
            adapter.items = state.streams
        }
    }

    override fun handleEffect(effect: StreamEffect): Unit = when (effect) {
        is StreamEffect.Error -> {
        }
        is StreamEffect.OpenChat -> setFragmentResult(
            resultKey, bundleOf(
                "streamName" to effect.streamName,
                "topicName" to effect.topicName,
                "streamId" to effect.streamId
            )
        )
    }

    fun reloadStreams() {
        store.accept(StreamEvent.Ui.SearchStreams(lastSearchQuery, isSubscribed))
    }

    override fun onDestroy() {
        _binding = null
        compositeDisposable.clear()
        super.onDestroy()
    }

    companion object {

        val searchSubject: PublishSubject<String> = PublishSubject.create()
        private const val ARG_RESULT_KEY = "ARG_RESULT_KEY"
        private const val IS_SUBSCRIBED_STREAMS = "IS_SUBSCRIBED_STREAMS"
        private const val COUNT_SHIMMERS_FOR_LOADING = 10

        fun newInstance(resultKey: String, isSubscribed: Boolean): Fragment {
            return StreamsFragment().apply {
                val bundle = bundleOf(
                    ARG_RESULT_KEY to resultKey,
                    IS_SUBSCRIBED_STREAMS to isSubscribed
                )
                arguments = bundle
            }
        }
    }

}