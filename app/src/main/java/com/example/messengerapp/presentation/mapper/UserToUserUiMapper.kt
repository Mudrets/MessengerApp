package com.example.messengerapp.presentation.mapper

import com.example.messengerapp.domain.entity.user.User
import com.example.messengerapp.presentation.recyclerview.user.UserUi

interface UserToUserUiMapper : (User) -> UserUi

