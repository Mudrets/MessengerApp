package com.example.messengerapp.domain.usecase.channel

import com.example.messengerapp.domain.entity.channel.Stream
import com.example.messengerapp.domain.repository.ChannelRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

interface SearchStreamsUseCase : (String, Boolean) -> Observable<List<Stream>>

class SearchStreamsUseCaseImpl @Inject constructor(
    private val repository: ChannelRepository
) : SearchStreamsUseCase {

    private fun loadStreams(isSubscribed: Boolean): Observable<List<Stream>> =
        if (isSubscribed)
            repository.loadSubscribedStreams()
        else
            repository.loadAllStreams()

    override fun invoke(
        searchQuery: String,
        isSubscribed: Boolean
    ): Observable<List<Stream>> =
        loadStreams(isSubscribed)
            .map { streams ->
                val filteredStreams = streams.filter { stream ->
                    stream.name.contains(searchQuery, ignoreCase = true)
                }
                filteredStreams
            }
}