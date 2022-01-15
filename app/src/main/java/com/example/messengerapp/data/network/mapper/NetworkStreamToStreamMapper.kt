package com.example.messengerapp.data.network.mapper

import com.example.messengerapp.data.network.dto.StreamResponse
import com.example.messengerapp.domain.entity.channel.Stream
import io.reactivex.rxjava3.core.Single

interface NetworkStreamToStreamMapper : (StreamResponse.Stream) -> Single<Stream>