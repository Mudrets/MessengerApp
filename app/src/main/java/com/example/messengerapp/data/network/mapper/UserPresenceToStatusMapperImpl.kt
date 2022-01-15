package com.example.messengerapp.data.network.mapper

import com.example.messengerapp.data.network.dto.UserPresenceResponse
import com.example.messengerapp.domain.entity.user.UserStatus
import javax.inject.Inject

class UserPresenceToStatusMapperImpl @Inject constructor() : UserPresenceToStatusMapper {

    override fun invoke(presence: UserPresenceResponse): UserStatus {
        val res = presence.presence
        val unixTime = System.currentTimeMillis() / 1000
        var lastActiveTime = 0
        var lastIdleTime = 0
        if (res.website != null) {
            when (res.website.status) {
                "active" -> lastActiveTime = maxOf(lastActiveTime, res.website.timestamp)
                "idle" -> lastIdleTime = maxOf(lastIdleTime, res.website.timestamp)
                else -> {
                }
            }
        }
        if (res.aggregated != null) {
            when (res.aggregated.status) {
                "active" -> lastActiveTime = maxOf(lastActiveTime, res.aggregated.timestamp)
                "idle" -> lastIdleTime = maxOf(lastIdleTime, res.aggregated.timestamp)
                else -> {
                }
            }
        }
        if (res.zulipDesktop != null) {
            when (res.zulipDesktop.status) {
                "active" -> lastActiveTime = maxOf(lastActiveTime, res.zulipDesktop.timestamp)
                "idle" -> lastIdleTime = maxOf(lastIdleTime, res.zulipDesktop.timestamp)
                else -> {
                }
            }
        }
        if (res.zulipMobile != null) {
            when (res.zulipMobile.status) {
                "active" -> lastActiveTime = maxOf(lastActiveTime, res.zulipMobile.timestamp)
                "idle" -> lastIdleTime = maxOf(lastIdleTime, res.zulipMobile.timestamp)
                else -> {
                }
            }
        }
        if (res.zulipTerminal != null) {
            when (res.zulipTerminal.status) {
                "active" -> lastActiveTime = maxOf(lastActiveTime, res.zulipTerminal.timestamp)
                "idle" -> lastIdleTime = maxOf(lastIdleTime, res.zulipTerminal.timestamp)
                else -> {
                }
            }
        }
        return if (unixTime - 60 <= lastActiveTime) {
            UserStatus.ACTIVE
        } else {
            if (unixTime - 60 <= lastIdleTime) {
                UserStatus.IDLE
            } else {
                UserStatus.OFFLINE
            }
        }
    }

}