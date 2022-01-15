package com.example.messengerapp.util.ext

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun <T : View> View.inflate(
    @LayoutRes
    layout: Int,
    root: ViewGroup? = this as? ViewGroup,
    attachToRoot: Boolean = false
): T = LayoutInflater.from(context).inflate(layout, root, attachToRoot) as T