package com.example.messengerapp.di

import android.content.Context
import androidx.room.Room
import com.example.messengerapp.data.db.AppDatabase
import com.example.messengerapp.data.db.dao.MessageDao
import com.example.messengerapp.data.db.dao.StreamDao
import com.example.messengerapp.util.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun provideStreamDao(db: AppDatabase): StreamDao = db.streamDao()

    @Singleton
    @Provides
    fun provideMessageDao(db: AppDatabase): MessageDao = db.messageDao()

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase) = db.userDao()

    @Singleton
    @Provides
    fun provideEmojiDao(db: AppDatabase) = db.emojiDao()

    @Singleton
    @Provides
    fun provideDataBase(context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        Constants.DATA_BASE_NAME
    ).build()

}