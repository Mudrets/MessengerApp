package com.example.messengerapp.presentation.recyclerview.base

import com.example.messengerapp.R

data class NextItemLoader(
    override val viewType: Int = R.layout.item_loader,
    override val uid: String = "nextPageLoader"
) : ViewTyped