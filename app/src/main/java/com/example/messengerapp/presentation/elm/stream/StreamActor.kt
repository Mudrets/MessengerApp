package com.example.messengerapp.presentation.elm.stream

import com.example.messengerapp.domain.usecase.channel.SearchStreamsUseCase
import com.example.messengerapp.presentation.elm.stream.model.StreamCommand
import com.example.messengerapp.presentation.elm.stream.model.StreamEvent
import com.example.messengerapp.presentation.mapper.StreamToStreamUiMapper
import com.example.messengerapp.util.Constants
import io.reactivex.rxjava3.core.Observable
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class StreamActor @Inject constructor(
    private val searchStreamsUseCase: SearchStreamsUseCase,
    private val streamToStreamUiMapper: StreamToStreamUiMapper
) : Actor<StreamCommand, StreamEvent> {

    private var currQuery = Constants.INITIAL_QUERY

    override fun execute(command: StreamCommand): Observable<StreamEvent> = when (command) {
        is StreamCommand.SearchStreams -> {
            var responseNum = 0
            currQuery = command.searchQuery
            searchStreamsUseCase(command.searchQuery, command.isSubscribed)
                .map { streams ->
                    streams.map(streamToStreamUiMapper).sortedWith { stream1, stream2 ->
                        stream1.streamName.compareTo(stream2.streamName, ignoreCase = true)
                    }
                }
                .mapEvents(
                    { list ->
                        responseNum++
                        if (currQuery == command.searchQuery) {
                            if (responseNum == 1 && list.isEmpty()) {
                                StreamEvent.Internal.ShowLoad
                            } else {
                                StreamEvent.Internal.StreamLoaded(list)
                            }
                        } else {
                            StreamEvent.Internal.Nothing
                        }
                    },
                    { error -> StreamEvent.Internal.ErrorSearching(error) }
                )
        }
    }
}