package com.example.messengerapp.presentation.elm.profile

import com.example.messengerapp.presentation.elm.profile.model.ProfileState
import com.example.messengerapp.util.ext.fastLazy
import vivid.money.elmslie.core.store.ElmStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileStoreFactory @Inject constructor(
    private val reducer: ProfileReducer,
    private val actor: ProfileActor
) {

    private val store by fastLazy {
        ElmStore(
            initialState = ProfileState.Loading,
            reducer = reducer,
            actor = actor
        )
    }

    fun provide() = store
}