package com.example.messengerapp.presentation.fragment.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.R
import com.example.messengerapp.databinding.FragmentChatBinding
import com.example.messengerapp.di.chat.ChatComponent
import com.example.messengerapp.di.chat.DaggerChatComponent
import com.example.messengerapp.presentation.dialog.command.ChatDialogType
import com.example.messengerapp.presentation.dialog.command.DialogCommand
import com.example.messengerapp.presentation.dialog.manager.DialogManager
import com.example.messengerapp.presentation.elm.chat.model.ChatCommand
import com.example.messengerapp.presentation.elm.chat.model.ChatEffect
import com.example.messengerapp.presentation.elm.chat.model.ChatEvent
import com.example.messengerapp.presentation.elm.chat.model.ChatState
import com.example.messengerapp.presentation.models.ChangeMessageReactionRequest
import com.example.messengerapp.presentation.models.ChangeReactionType
import com.example.messengerapp.presentation.models.MessageAction
import com.example.messengerapp.presentation.recyclerview.chat.ChatAsyncAdapter
import com.example.messengerapp.presentation.recyclerview.chat.MessageUi
import com.example.messengerapp.util.ext.appComponent
import com.example.messengerapp.util.ext.fastLazy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

abstract class ChatFragment : ElmFragment<ChatEvent, ChatEffect, ChatState>() {

    protected val topicName: String by fastLazy { requireArguments().getString(TOPIC_NAME_KEY, "") }
    protected val streamName: String by fastLazy {
        requireArguments().getString(
            STREAM_NAME_KEY,
            ""
        )
    }
    protected val streamId by fastLazy { requireArguments().getLong(STREAM_ID_KEY) }
    protected val resultKey by fastLazy { requireArguments().getString(RES_KEY, "")!! }

    private var _binding: FragmentChatBinding? = null
    val binding: FragmentChatBinding
        get() = _binding!!

    lateinit var chatComponent: ChatComponent

    private val attachDrawableId = R.drawable.ic_baseline_attach_file_24
    private val sendDrawableId = R.drawable.ic_baseline_send_24

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    @Inject
    lateinit var adapter: ChatAsyncAdapter

    @Inject
    lateinit var chatStore: ElmStore<ChatEvent, ChatState, ChatEffect, ChatCommand>

    @Inject
    lateinit var topicDelimiterClickSubject: PublishSubject<String>

    @Inject
    lateinit var changeReactionSubject: PublishSubject<ChangeMessageReactionRequest>

    @Inject
    lateinit var selectMessageActionSubject: PublishSubject<MessageUi>

    @Inject
    lateinit var selectReactionSubject: PublishSubject<Long>

    @Inject
    lateinit var dialogManager: DialogManager<ChatDialogType>

    protected var layoutManager: RecyclerView.LayoutManager? = null

    private var messageId = -1L

    override fun onAttach(context: Context) {
        chatComponent = DaggerChatComponent.builder()
            .appComponent(context.appComponent)
            .fragmentManager(childFragmentManager)
            .topicName(topicName)
            .streamId(streamId)
            .build()
        chatComponent.inject(this)
        super.onAttach(context)
    }

    abstract val onScrollListener: RecyclerView.OnScrollListener

    abstract fun sendMessage(messageText: String, messageId: Long)

    open fun topicDelimiterClick(topicName: String) {}

    open fun initUi() {
        binding.apply {
            activity?.window?.statusBarColor = resources.getColor(R.color.aquamarine, null)

            (activity as AppCompatActivity).setSupportActionBar(chatToolbar)
            chatToolbar.setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }

            topicNameContainer.isVisible = false
            chatTopicName.isVisible = false
            chatToolbar.title = resources.getString(R.string.stream_title, streamName)

            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
            messageRecyclerView.layoutManager = layoutManager
            messageRecyclerView.addOnScrollListener(onScrollListener)
            messageRecyclerView.adapter = adapter
            adapter.addNewElementToStartCallback = {
                messageRecyclerView.smoothScrollToPosition(0)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeReactionSubject
            .subscribeBy { request -> changeReaction(request) }
            .addTo(compositeDisposable)

        selectMessageActionSubject
            .subscribeBy { messageUi -> showDialog(ChatDialogType.MESSAGE_ACTION, messageUi) }
            .addTo(compositeDisposable)

        topicDelimiterClickSubject
            .subscribeBy { topicName -> topicDelimiterClick(topicName) }
            .addTo(compositeDisposable)

        selectReactionSubject
            .subscribeBy { messageId -> showDialog(ChatDialogType.EMOJI_PICKER, messageId) }
            .addTo(compositeDisposable)

        if (savedInstanceState == null)
            store.accept(ChatEvent.Ui.LoadFirstPage(streamId, topicName))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        dialogManager.lifecycleOwner = viewLifecycleOwner
        initUi()
        setListeners()
        setDialogListeners()

        return binding.root
    }

    override fun onDestroyView() {
        dialogManager.lifecycleOwner = null
        super.onDestroyView()
    }

    override val initEvent: ChatEvent = ChatEvent.Ui.Init

    override fun createStore(): Store<ChatEvent, ChatEffect, ChatState> = chatStore

    private fun showLoad() {
        normalState()
        binding.apply {
            messageRecyclerView.isVisible = false
            topicNameContainer.isVisible = false
            chatShimmerContainer.isVisible = true
            chatShimmerContainer.startShimmer()
        }
    }

    private fun showError() {
        normalState()
        binding.apply {
            messageRecyclerView.isVisible = false
            topicNameContainer.isVisible = false
            chatErrorText.isVisible = true
            errorChatImage.isVisible = true
            reloadChatButton.isVisible = true
        }
    }

    private fun showWarningAboutEmptyList() {
        normalState()
        binding.apply {
            messageRecyclerView.isVisible = false
            topicNameContainer.isVisible = false
            emptyChatListImage.isVisible = true
            emptyChatListText.isVisible = true
        }
    }

    open fun normalState() {
        binding.apply {
            messageRecyclerView.isVisible = true
            chatErrorText.isVisible = false
            errorChatImage.isVisible = false
            emptyChatListImage.isVisible = false
            emptyChatListText.isVisible = false
            chatShimmerContainer.isVisible = false
            reloadChatButton.isVisible = false
            chatShimmerContainer.stopShimmer()
        }
    }

    final override fun render(state: ChatState) {
        state.apply {
            when {
                error != null -> showError()
                isEmptyState -> showWarningAboutEmptyList()
                isLoading -> showLoad()
                else -> {
                    adapter.items = messages
                    normalState()
                }
            }
        }
    }

    private fun showDialog(actionType: ChatDialogType, data: Any) {
        dialogManager.showDialog(
            DialogCommand(
                type = actionType,
                data = bundleOf(DialogManager.ACTION_DATA_KEY to data)
            )
        )
    }

    final override fun handleEffect(effect: ChatEffect): Unit = when (effect) {
        is ChatEffect.ErrorEffect -> dialogManager.showError(effect.th)
    }

    private fun actionMapper(action: MessageAction, messageUi: MessageUi) = when (action) {
        MessageAction.ADD_REACTION -> showDialog(ChatDialogType.EMOJI_PICKER, messageUi.messageId)
        MessageAction.CHANGE_TOPIC -> showDialog(ChatDialogType.CHANGE_TOPIC, messageUi)
        MessageAction.COPY -> copyToClipboard(
            messageUi.messageText,
            getString(R.string.message_after_copy_message_text)
        )
        MessageAction.DELETE ->
            store.accept(ChatEvent.Ui.DeleteMessage(messageUi.messageId))
        MessageAction.EDIT -> {
            binding.messageEditText.setText(messageUi.messageText)
            binding.topicNameContainer.isVisible = false
            messageId = messageUi.messageId
        }
    }

    private fun changeReaction(request: ChangeMessageReactionRequest) {
        val messageId = request.messageId
        val emojiName = request.emojiName
        when (request.actionType) {
            ChangeReactionType.ADD -> store.accept(ChatEvent.Ui.AddReaction(messageId, emojiName))
            ChangeReactionType.REMOVE -> store.accept(
                ChatEvent.Ui.RemoveReaction(
                    messageId,
                    emojiName
                )
            )
        }
    }

    private fun copyToClipboard(string: String, message: String = "") {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("", string)
        clipboard.setPrimaryClip(clip)
        if (message.isNotBlank())
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun setDialogListeners() {
        dialogManager.addListener(ChatDialogType.MESSAGE_ACTION) { bundle ->
            val messageUi = bundle.getParcelable<MessageUi>("message")
            val action = bundle.getParcelable<MessageAction>("action")
            if (messageUi != null && action != null)
                actionMapper(action, messageUi)
        }

        dialogManager.addListener(ChatDialogType.EMOJI_PICKER) { bundle ->
            val messageId = bundle.getLong("messageId")
            val emojiName = bundle.getString("selectedEmojiName", "")
            store.accept(ChatEvent.Ui.AddReaction(messageId, emojiName))
        }

        dialogManager.addListener(ChatDialogType.CHANGE_TOPIC) { bundle ->
            val message = bundle.getParcelable<MessageUi>("message")
            val newTopicName = bundle.getString("topicName", "")
            if (newTopicName.isNotBlank() && message != null && newTopicName != message.topicName)
                store.accept(ChatEvent.Ui.ChangeTopic(message.messageId, newTopicName))
        }

        dialogManager.setListeners()
    }

    private fun setListeners() {
        binding.apply {
            sendMessageButton.setOnClickListener {
                if (!store.currentState.isLoading && messageEditText.text.isNotBlank())
                    sendMessage(messageEditText.text.toString(), messageId)
                else if (messageEditText.text.isBlank())
                    Toast.makeText(
                        context,
                        getString(R.string.empty_message_edittext_warning),
                        Toast.LENGTH_LONG
                    ).show()
            }

            messageEditText.addTextChangedListener { editText ->
                if (editText != null) {
                    val sendDrawable =
                        ResourcesCompat.getDrawable(resources, sendDrawableId, null)
                    val attachDrawable =
                        ResourcesCompat.getDrawable(resources, attachDrawableId, null)
                    val sendButtonIcon = sendMessageButton.icon

                    if (editText.toString().isNotBlank() && sendButtonIcon != sendDrawable) {
                        sendMessageButton.icon = sendDrawable
                    } else if (editText.toString().isBlank()) {
                        sendMessageButton.icon = attachDrawable
                    }
                }
            }

            reloadChatButton.setOnClickListener {
                store.accept(ChatEvent.Ui.LoadFirstPage(streamId, topicName))
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        compositeDisposable.clear()
        super.onDestroy()
    }

    companion object {

        private const val RES_KEY = "CHAT_RES_KEY"
        private const val TOPIC_NAME_KEY = "TOPIC_NAME"
        private const val STREAM_NAME_KEY = "STREAM_NAME"
        private const val STREAM_ID_KEY = "STREAM_ID"

        fun newInstance(
            streamName: String,
            streamId: Long,
            topicName: String = "",
            chatResKey: String = ""
        ): Fragment {
            return when {
                topicName.isNotBlank() -> TopicChatFragment()
                else -> StreamChatFragment()
            }
                .apply {
                    arguments = bundleOf(
                        TOPIC_NAME_KEY to topicName,
                        STREAM_NAME_KEY to streamName,
                        STREAM_ID_KEY to streamId,
                        RES_KEY to chatResKey
                    )
                }
        }
    }
}