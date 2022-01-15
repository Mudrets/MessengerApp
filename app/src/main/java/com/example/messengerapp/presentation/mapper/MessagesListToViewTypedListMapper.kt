package com.example.messengerapp.presentation.mapper

import com.example.messengerapp.domain.entity.chat.Message
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped

interface MessagesListToViewTypedListMapper : (List<Message>) -> List<ViewTyped>