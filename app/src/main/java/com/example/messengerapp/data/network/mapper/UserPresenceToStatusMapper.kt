package com.example.messengerapp.data.network.mapper

import com.example.messengerapp.data.network.dto.UserPresenceResponse
import com.example.messengerapp.domain.entity.user.UserStatus

interface UserPresenceToStatusMapper : (UserPresenceResponse) -> UserStatus