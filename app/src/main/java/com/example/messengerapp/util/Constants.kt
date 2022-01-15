package com.example.messengerapp.util

import com.example.messengerapp.R
import com.example.messengerapp.presentation.viewpager.stream.StreamPageInfo
import com.example.messengerapp.presentation.viewpager.stream.StreamPageType
import com.example.messengerapp.util.ext.fastLazy

object Constants {

    val emojiList by fastLazy { }

    val STREAM_PAGES = listOf(
        StreamPageInfo(R.string.subscribed_title, StreamPageType.SUBSCRIBED),
        StreamPageInfo(R.string.all_streams_title, StreamPageType.ALL_STREAMS)
    )

    val COLORS = listOf(R.color.aquamarine, R.color.yellow)

    const val INITIAL_QUERY = ""

    const val ALL_STREAMS = "ALL_STREAMS"
    const val SUBSCRIBED = "SUBSCRIBED"
    const val MIN_PAGE_SIZE = 20
    const val MESSAGE_THRESHOLD = 5
    const val STREAM_CHAT_MESSAGE_THRESHOLD = 5
    const val DATA_BASE_NAME = "db"

    const val ZULIP_URL = "https://tinkoff-android-fall21.zulipchat.com"
    const val API_URL = "$ZULIP_URL/api/v1/"
    const val USERNAME = "nazroman2002@gmail.com"
    const val API_KEY = "wE1DRggJqzvIK9P3yPa2cpONDSevm5zy"
    const val DB_EMAIL = "user456163@tinkoff-android-fall21.zulipchat.com"

    val IGNORED_EMOJI_LIST = listOf(
        "sad",
        "skull_and_crossbones",
        "writing",
        "helmet",
        "umbrella",
        "comet",
        "thunderstorm",
        "snowman",
        "shamrock",
        "ice_skate",
        "skier",
        "ball",
        "airplane",
        "ferry",
        "black_medium_square",
        "white_medium_square",
        "checkbox",
        "check_mark",
        "beach_umbrella",
        "mountain",
        "shinto_shrine",
        "keyboard",
        "stopwatch",
        "timer",
        "justice",
        "at_work",
        "mine",
        "upper_right",
        "lower_right",
        "lower_left",
        "upper_left",
        "up_down",
        "reply",
        "heading_up",
        "heading_down",
        "chains",
        "gear",
        "up",
        "right",
        "play_reverse",
        "previous_track",
        "next_track",
        "play_pause",
        "play",
        "parking",
        "metro",
        "eight_spoked_asterisk",
        "eight_pointed_star",
        "a",
        "b",
        "o",
        "hot_springs",
        "part_alternation",
        "warning",
        "fleur_de_lis",
        "recycle",
        "sparkle",
        "japanese_congratulations_button",
        "japanese_monthly_amount_button",
        "biohazard",
        "radioactive",
        "atom",
        "orthodox_cross",
        "yin_yang",
        "star_of_david",
        "wheel_of_dharma",
        "heart_exclamation",
        "pencil",
        "scissors",
        "secret",
        "alchemy",
        "funeral_urn",
        "coffin",
        "multiplication",
        "down",
        "cross",
        "star_and_crescent",
        "peace",
        "bangbang",
        "interrobang",
        "wavy_dash",
        "black_small_square",
        "white_small_square",
        "left_right",
        "forward",
        "left",
        "tm",
        "info",
        "duel",
        "email"
    )
}