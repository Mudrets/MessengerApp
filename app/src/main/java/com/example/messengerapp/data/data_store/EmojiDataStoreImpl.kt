package com.example.messengerapp.data.data_store

import com.example.messengerapp.data.db.dao.EmojiDao
import com.example.messengerapp.data.db.entities.EmojiDb
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class EmojiDataStoreImpl @Inject constructor(
    private val emojiDao: EmojiDao
) : EmojiDataStore {
    override fun insertEmojiData(emojiMap: Map<String, String>) {
        val emojiDbList = emojiMap.map { (emojiName, emojiCode) ->
            EmojiDb(
                emojiName = emojiName,
                emojiCode = emojiCode
            )
        }
        emojiDao.insertEmojiData(emojiDbList)
    }

    override fun getEmojiData(): Single<Map<String, String>> =
        emojiDao.getEmojiData()
            .subscribeOn(Schedulers.io())
            .map { emojiDbList ->
                val map = mutableMapOf<String, String>()
                emojiDbList.forEach { emojiDb ->
                    map[emojiDb.emojiName] = emojiDb.emojiCode
                }
                map
            }
}