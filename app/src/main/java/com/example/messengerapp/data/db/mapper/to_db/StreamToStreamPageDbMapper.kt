package com.example.messengerapp.data.db.mapper.to_db

import com.example.messengerapp.data.db.entities.StreamPageDb
import com.example.messengerapp.domain.entity.channel.Stream
import com.example.messengerapp.util.Constants.ALL_STREAMS
import com.example.messengerapp.util.Constants.SUBSCRIBED
import javax.inject.Inject

interface StreamToStreamPageDbMapper : (Stream, Boolean) -> StreamPageDb

class StreamToStreamPageDbMapperImpl @Inject constructor() : StreamToStreamPageDbMapper {
    override fun invoke(stream: Stream, isSubscribed: Boolean): StreamPageDb =
        StreamPageDb(
            streamId = stream.streamId,
            name = if (isSubscribed) SUBSCRIBED else ALL_STREAMS
        )

}