package com.example.messengerapp.di

import com.example.messengerapp.data.db.mapper.from_db.UserDbToUserMapper
import com.example.messengerapp.data.db.mapper.from_db.UserDbToUserMapperImpl
import com.example.messengerapp.data.db.mapper.to_db.UserToUserDbMapper
import com.example.messengerapp.data.db.mapper.to_db.UserToUserDbMapperImpl
import com.example.messengerapp.data.network.mapper.NetworkUserToUserMapper
import com.example.messengerapp.data.network.mapper.NetworkUserToUserMapperImpl
import com.example.messengerapp.data.network.mapper.UserPresenceToStatusMapper
import com.example.messengerapp.data.network.mapper.UserPresenceToStatusMapperImpl
import com.example.messengerapp.presentation.mapper.UserToUserUiMapper
import com.example.messengerapp.presentation.mapper.UserToUserUiMapperImpl
import dagger.Binds
import dagger.Module

@Module
interface UserMappersBindModule {

    @Binds
    fun bindUserToUserDbMapperImpl_to_UserToUserDbMapper(
        userToUserDbMapperImpl: UserToUserDbMapperImpl
    ): UserToUserDbMapper

    @Binds
    fun bindUserDbToUserMapperImpl_to_UserDbToUserMapper(
        userDbToUserMapperImpl: UserDbToUserMapperImpl
    ): UserDbToUserMapper

    @Binds
    fun bindUserPresenceToStatusMapperImpl_to_UserPresenceToStatusMapper(
        userPresenceToStatusMapperImpl: UserPresenceToStatusMapperImpl
    ): UserPresenceToStatusMapper

    @Binds
    fun bindNetworkUserToUserMapperImpl_to_NetworkUserToUserMapper(
        networkUserToUserMapperImpl: NetworkUserToUserMapperImpl
    ): NetworkUserToUserMapper

    @Binds
    fun bindUserToUserUiMapperImpl_to_UserToUserUiMapper(
        userToUserUiMapperImpl: UserToUserUiMapperImpl
    ): UserToUserUiMapper
}