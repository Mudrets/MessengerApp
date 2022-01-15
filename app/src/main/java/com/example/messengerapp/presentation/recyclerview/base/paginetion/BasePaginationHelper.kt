package com.example.messengerapp.presentation.recyclerview.base.paginetion

abstract class BasePaginationHelper<T> : PaginationHelper<T> {

    private val startIndexOfPages: MutableList<Int> = mutableListOf(0)

    val size: Int
        get() = startIndexOfPages.size - 1

    open fun addNewPage(newItems: List<T>) {
        startIndexOfPages.add(startIndexOfPages.last() + newItems.size)
    }

    open fun replacePage(newItems: List<T>, pageIndex: Int) {
        changeSizeOfPage(pageIndex, newItems.size)
    }

    final override fun set(pageIndex: Int, newElements: List<T>) {
        if (startIndexOfPages.lastIndex <= pageIndex)
            addNewPage(newElements)
        else
            replacePage(newElements, pageIndex)
    }

    final override fun get(pageIndex: Int): List<T> {
        if (pageIndex >= size || pageIndex < 0)
            throw IndexOutOfBoundsException("Index: $pageIndex, Size: $size")
        return items.subList(startIndexOfPages[pageIndex], startIndexOfPages[pageIndex + 1])
    }

    private fun changeSizeOfPage(pageIndex: Int, newSize: Int) {
        if (pageIndex + 1 < startIndexOfPages.size) {
            val oldSize = startIndexOfPages[pageIndex + 1] - startIndexOfPages[pageIndex]
            val sizeDiffer = newSize - oldSize
            for (i in (pageIndex + 1)..startIndexOfPages.lastIndex)
                startIndexOfPages[i] += sizeDiffer
        }
    }

    override fun getItemPageIndex(item: T): Int {
        for (i in 0 until size)
            if (get(i).contains(item))
                return i
        return -1
    }

    override fun getItemPageIndex(predicate: (T) -> Boolean): Int {
        val itemIndex = items.indexOfFirst(predicate)
        for (i in 0 until startIndexOfPages.lastIndex)
            if (itemIndex in startIndexOfPages[i] until startIndexOfPages[i + 1])
                return i
        return -1
    }

    override fun clear() {
        startIndexOfPages.clear()
        startIndexOfPages.add(0)
    }

    protected fun getPageBounds(pageIndex: Int) =
        startIndexOfPages[pageIndex] to startIndexOfPages[pageIndex + 1] - 1

}