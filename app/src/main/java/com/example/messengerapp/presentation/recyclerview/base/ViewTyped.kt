package com.example.messengerapp.presentation.recyclerview.base

/**
 * Интерфейс эелемента адаптера
 */
interface ViewTyped {

    /**
     * Layout элемента
     */
    val viewType: Int
        get() = error("provide viewType $this")

    /**
     * Уникальный идентификатор для работ
     */
    val uid: String
        get() = error("provide uid for viewType $this")

}