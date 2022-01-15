package com.example.messengerapp.util.ext

import android.content.Context
import com.example.messengerapp.App
import com.example.messengerapp.di.AppComponent

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }