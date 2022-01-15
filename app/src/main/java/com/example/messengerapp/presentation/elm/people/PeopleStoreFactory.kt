package com.example.messengerapp.presentation.elm.people

import com.example.messengerapp.presentation.elm.people.model.PeopleState
import com.example.messengerapp.util.ext.fastLazy
import vivid.money.elmslie.core.store.ElmStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeopleStoreFactory @Inject constructor(
    private val actor: PeopleActor,
    private val reducer: PeopleReducer
) {
    private val store by fastLazy {
        ElmStore(
            initialState = PeopleState.Loading,
            reducer = reducer,
            actor = actor
        )
    }

    fun provide() = store
}