package com.example.messengerapp.presentation.elm.main

import com.example.messengerapp.presentation.elm.main.model.MainState
import com.example.messengerapp.util.ext.fastLazy
import vivid.money.elmslie.core.store.ElmStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainStoreFactory @Inject constructor(
    private val actor: MainActor,
    private val reducer: MainReducer
) {
    private val store by fastLazy {
        ElmStore(
            initialState = MainState.Loading,
            reducer = reducer,
            actor = actor
        )
    }

    fun provide() = store
}