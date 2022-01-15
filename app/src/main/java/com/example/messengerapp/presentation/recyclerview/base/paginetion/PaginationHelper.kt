package com.example.messengerapp.presentation.recyclerview.base.paginetion

interface PaginationHelper<T> {
    val items: List<T>

    operator fun set(pageIndex: Int, newElements: List<T>)

    operator fun get(pageIndex: Int): List<T>

    fun clear()

    fun showLoad()

    fun removeLoad()

    fun getItemPageIndex(item: T): Int

    fun getItemPageIndex(predicate: (T) -> Boolean): Int

}