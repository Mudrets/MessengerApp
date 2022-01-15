package com.example.messengerapp.presentation.recyclerview.base.composite

import com.example.messengerapp.presentation.recyclerview.base.ViewTyped

interface CompositeViewTyped : ViewTyped {

    /**
     * Вложенные эелементы
     */
    val items: List<ViewTyped>
        get() = error("provide items $this")

}