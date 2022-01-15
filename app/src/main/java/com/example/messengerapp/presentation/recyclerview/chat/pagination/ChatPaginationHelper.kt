package com.example.messengerapp.presentation.recyclerview.chat.pagination

import com.example.messengerapp.presentation.recyclerview.base.NextItemLoader
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped
import com.example.messengerapp.presentation.recyclerview.base.paginetion.BasePaginationHelper
import com.example.messengerapp.presentation.recyclerview.chat.DateUi
import com.example.messengerapp.presentation.recyclerview.chat.MessageUi
import com.example.messengerapp.presentation.recyclerview.chat.TopicDelimiter
import javax.inject.Inject

class ChatPaginationHelper @Inject constructor() : BasePaginationHelper<ViewTyped>() {

    var isLoading: Boolean = false
        private set
    private var _items: MutableList<ViewTyped> = mutableListOf()
    override val items: List<ViewTyped>
        get() = listOf<ViewTyped>() + _items

    override fun clear() {
        super.clear()
        _items.clear()
    }

    override fun addNewPage(newItems: List<ViewTyped>) {
        val filteredList = filterElement(size, newItems)
        super.addNewPage(filteredList)
        _items.addAll(filteredList)
    }

    fun addNewItems(newElements: List<ViewTyped>) {
        val firstPage = get(0)
        val newPage = newElements + firstPage
        replacePage(newPage, 0)
    }

    override fun replacePage(newItems: List<ViewTyped>, pageIndex: Int) {
        val filteredList = filterElement(pageIndex, newItems)
        val (startIndex, endIndex) = getPageBounds(pageIndex)
        _items = _items.filterIndexed { index, _ -> index !in startIndex..endIndex }.toMutableList()
        _items.addAll(startIndex, filteredList)
        super.replacePage(filteredList, pageIndex)
    }

    override fun showLoad() {
        if (!isLoading) {
            isLoading = true
            _items.add(NextItemLoader())
        }
    }

    override fun removeLoad() {
        if (isLoading) {
            isLoading = false
            _items.remove(NextItemLoader())
        }
    }

    fun changeItem(index: Int, newItem: ViewTyped) {
        _items.removeAt(index)
        _items.add(index, newItem)
    }

    fun deleteItem(index: Int) {
        var pageIndex = -1
        var itemIndexInPage = index
        for (i in 0 until size) {
            val (start, end) = getPageBounds(i)
            if (index in start..end) {
                pageIndex = i
                itemIndexInPage -= start
                break
            }
        }
        if (pageIndex != -1) {
            val page = get(pageIndex)
            val newPage = page.filterIndexed { itemIndex, _ -> itemIndex != itemIndexInPage }
            set(pageIndex, newPage)
        }
    }

    private fun removeUselessItems(list: MutableList<ViewTyped>): List<ViewTyped> {
        while (list.isNotEmpty() && list[0] is TopicDelimiter)
            list.removeAt(0)
        var index = 1
        while (index < list.size) {
            if (list[index] is TopicDelimiter && list[index - 1] is TopicDelimiter)
                list.removeAt(index--)
            if (list[index] is DateUi && list[index - 1] is DateUi)
                list.removeAt(index--)
            index++
        }

        return list.asReversed().distinctBy { viewTyped ->
            viewTyped.uid
        }.asReversed().toMutableList()
    }

    private fun filterElement(pageIndex: Int, currPage: List<ViewTyped>): List<ViewTyped> {
        val pageBefore = if (pageIndex > 0) get(pageIndex - 1) else listOf()
        val pageAfter = if (pageIndex < size - 1) get(pageIndex + 1) else listOf()
        val newList = (pageBefore + currPage + pageAfter).toMutableList()

        val filteredList = removeUselessItems(newList)

        var newPageBefore = listOf<ViewTyped>()
        val newCurrPage: List<ViewTyped>
        var endIndex = filteredList.size
        val startIndex = filteredList.indexOf(currPage.find { it is MessageUi })

        if (pageIndex > 0 && startIndex != -1)
            newPageBefore = filteredList.subList(0, startIndex)

        if (pageIndex < size - 1 && startIndex != -1)
            endIndex = filteredList.indexOf(pageAfter[0])

        newCurrPage = if (startIndex != -1)
            filteredList.subList(startIndex, endIndex)
        else
            currPage

        if (newPageBefore.size < pageBefore.size)
            replacePage(newPageBefore, pageIndex - 1)

        return newCurrPage
    }
}