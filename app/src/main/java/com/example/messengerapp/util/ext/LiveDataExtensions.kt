package com.example.messengerapp.util.ext

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.set(newValue: T) = apply { value = newValue }

fun <T> MutableLiveData<List<T>>.add(elem: T) {
    val value = this.value ?: emptyList()
    set(value + listOf(elem))
}

