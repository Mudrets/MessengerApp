package com.example.messengerapp.presentation.dialog.command

import android.os.Bundle

data class DialogCommand<T : Enum<T>>(
    val type: T,
    val data: Bundle
)
