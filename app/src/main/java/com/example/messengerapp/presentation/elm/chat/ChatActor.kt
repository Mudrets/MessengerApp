package com.example.messengerapp.presentation.elm.chat

import com.example.messengerapp.domain.entity.chat.Message
import com.example.messengerapp.domain.usecase.chat.*
import com.example.messengerapp.presentation.elm.chat.model.ChatCommand
import com.example.messengerapp.presentation.elm.chat.model.ChatEvent
import com.example.messengerapp.presentation.mapper.MessagesListToViewTypedListMapper
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject
import javax.inject.Provider

class ChatActor @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val changeTopicUseCase: ChangeTopicUseCase,
    private val editMessageUseCase: EditMessageUseCase,
    private val messagesListToViewTypedListMapper: MessagesListToViewTypedListMapper,
    private val authorizedUserId: Provider<Long>
) : Actor<ChatCommand, ChatEvent> {
    override fun execute(command: ChatCommand): Observable<ChatEvent> = when (command) {
        is ChatCommand.SendMessage -> sendMessage(command)
        is ChatCommand.AddReaction -> addReaction(command)
        is ChatCommand.RemoveReaction -> removeReaction(command)
        is ChatCommand.LoadPage -> loadPage(command)
        is ChatCommand.DeleteMessage -> deleteMessage(command)
        is ChatCommand.ChangeTopic -> changeTopic(command)
        is ChatCommand.EditMessage -> editText(command)
    }

    private fun editText(command: ChatCommand.EditMessage): Observable<ChatEvent> =
        editMessageUseCase(command.messageId, command.content)
            .mapEvents(
                { isSuccess ->
                    if (isSuccess)
                        ChatEvent.Internal.MessageEdited(command.messageId, command.content)
                    else
                        ChatEvent.Internal.MessageActionError(
                            command.messageId,
                            Throwable("change topic")
                        )
                },
                { error ->
                    Timber.e(error)
                    ChatEvent.Internal.MessageActionError(
                        command.messageId,
                        Throwable("change topic")
                    )
                }
            )

    private fun changeTopic(command: ChatCommand.ChangeTopic): Observable<ChatEvent> =
        changeTopicUseCase(command.messageId, command.topicName)
            .mapEvents(
                { isSuccess ->
                    if (isSuccess)
                        ChatEvent.Internal.TopicChanged(command.messageId, command.topicName)
                    else
                        ChatEvent.Internal.MessageActionError(
                            command.messageId,
                            Throwable("change topic")
                        )
                },
                { error ->
                    Timber.e(error)
                    ChatEvent.Internal.MessageActionError(
                        command.messageId,
                        Throwable("change topic")
                    )
                }
            )

    private fun deleteMessage(command: ChatCommand.DeleteMessage): Observable<ChatEvent> =
        deleteMessageUseCase(command.messageId)
            .mapEvents(
                { isSuccess ->
                    if (isSuccess)
                        ChatEvent.Internal.MessageDeleted(command.messageId)
                    else
                        ChatEvent.Internal.MessageActionError(
                            command.messageId,
                            Throwable("delete message")
                        )
                },
                { error ->
                    Timber.e(error)
                    ChatEvent.Internal.MessageActionError(
                        command.messageId,
                        Throwable("delete message")
                    )
                }
            )

    private fun loadPage(command: ChatCommand.LoadPage): Observable<ChatEvent> {
        var responseNum = 0
        return getMessagesUseCase(command.pageNum, command.topicName, command.streamId)
            .map(messagesListToViewTypedListMapper)
            .mapEvents(
                { messages ->
                    responseNum++
                    if (responseNum == 1 && messages.isEmpty())
                        ChatEvent.Internal.Nothing
                    else
                        ChatEvent.Internal.PageLoaded(messages, command.pageNum)
                },
                { error -> ChatEvent.Internal.ErrorLoading(error, command.pageNum) }
            )
    }

    private fun removeReaction(command: ChatCommand.RemoveReaction): Observable<ChatEvent> =
        removeReactionUseCase(command.messageId, command.emojiName)
            .mapEvents(
                { isSuccess ->
                    if (isSuccess)
                        ChatEvent.Internal.RemoveReaction(command.messageId, command.emojiName)
                    else
                        ChatEvent.Internal.MessageActionError(
                            command.messageId,
                            Throwable("remove reaction")
                        )
                },
                { error ->
                    Timber.e(error)
                    ChatEvent.Internal.MessageActionError(
                        command.messageId,
                        Throwable("remove reaction")
                    )
                }
            )

    private fun addReaction(command: ChatCommand.AddReaction): Observable<ChatEvent> =
        addReactionUseCase(command.messageId, command.emojiName)
            .mapEvents(
                { isSuccess ->
                    if (isSuccess)
                        ChatEvent.Internal.AddReaction(command.messageId, command.emojiName)
                    else
                        ChatEvent.Internal.MessageActionError(
                            command.messageId,
                            Throwable("add reaction")
                        )
                },
                { error ->
                    Timber.e(error)
                    ChatEvent.Internal.MessageActionError(
                        command.messageId,
                        Throwable("add reaction")
                    )
                }
            )

    private fun sendMessage(command: ChatCommand.SendMessage): Observable<ChatEvent> =
        sendMessageUseCase(
            command.messageText,
            command.streamId,
            command.topicName,
            authorizedUserId.get()
        )
            .mapEvents(
                { messageId ->
                    val message = Message(
                        textMessage = command.messageText,
                        id = messageId,
                        isMyMessage = true,
                        senderEmail = ""
                    )
                    val viewType = messagesListToViewTypedListMapper(listOf(message))
                    ChatEvent.Internal.MessageSent(command.sendingId, viewType, messageId)
                },
                { error ->
                    Timber.e(error)
                    ChatEvent.Internal.ErrorMessageSent(command.sendingId)
                }
            )

}