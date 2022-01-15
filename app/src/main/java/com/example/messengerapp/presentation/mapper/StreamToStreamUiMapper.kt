package com.example.messengerapp.presentation.mapper

import com.example.messengerapp.domain.entity.channel.Stream
import com.example.messengerapp.presentation.recyclerview.stream.StreamUi

interface StreamToStreamUiMapper : (Stream) -> StreamUi