package com.example.messengerapp.data.data_provider

import com.example.messengerapp.data.network.ZulipApi
import com.example.messengerapp.util.Constants
import com.example.messengerapp.util.ext.convertStringToEmojiCode
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class EmojiDataProviderImpl @Inject constructor(
    private val zulipApi: ZulipApi
) : EmojiDataProvider {
    override fun getEmojiData(): Single<Map<String, String>> = zulipApi.getAllEmoji()
        .subscribeOn(Schedulers.io())
        .map { response ->
            response.nameToCodepoint
        }
        .map { map ->
            val newMap = mutableMapOf<String, String>()
            val ignoreList = mutableListOf<String>()
            map.forEach { (key, value) ->
                if (!Constants.IGNORED_EMOJI_LIST.contains(key) && !ignoreList.contains(value)) {
                    newMap[key] = value.convertStringToEmojiCode()
                } else {
                    ignoreList.add(map[key]!!)
                }
            }
            newMap.toMap()
        }
}