package com.example.messengerapp.data.network.mapper

import com.example.messengerapp.data.network.dto.NetworkUser
import com.example.messengerapp.domain.entity.user.User

interface NetworkUserToUserMapper : (NetworkUser) -> User