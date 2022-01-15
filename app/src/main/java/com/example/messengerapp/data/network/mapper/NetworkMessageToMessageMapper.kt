package com.example.messengerapp.data.network.mapper

import com.example.messengerapp.data.network.dto.MessagesResponse
import com.example.messengerapp.domain.entity.chat.Message

interface NetworkMessageToMessageMapper : (MessagesResponse.Message) -> Message