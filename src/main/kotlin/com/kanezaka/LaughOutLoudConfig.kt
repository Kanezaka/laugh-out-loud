package com.kanezaka

import net.runelite.client.config.Config
import net.runelite.client.config.ConfigGroup
import net.runelite.client.config.ConfigItem

@ConfigGroup("laughoutloud")
@JvmDefaultWithCompatibility
interface LaughOutLoudConfig : Config {
    @ConfigItem(
        keyName = "chuckles",
        name = "Additional Chuckles",
        description = "Messages that yield laughs!"
    )
    fun chuckles(): String? {
        return "teehee,heehee"
    }

    @ConfigItem(
        keyName = "pubChat",
        name = "Public Chat",
        description = "Laugh along with public chat?",
    )
    fun publicChat(): Boolean {
        return true
    }

    @ConfigItem(
        keyName = "privateChat",
        name = "Private Chat",
        description = "Laugh along with private messages?"
    )
    fun privateChat(): Boolean {
        return true
    }

    @ConfigItem(
        keyName = "friendsChat",
        name = "Friends Chat",
        description = "Laugh along with friends chat?"
    )
    fun friendsChat(): Boolean {
        return true
    }

    @ConfigItem(
        keyName = "clanChat",
        name = "Clan Chat",
        description = "Laugh along with clan chat?"
    )
    fun clanChat(): Boolean {
        return true
    }

    @ConfigItem(
        keyName = "gimChat",
        name = "GIM Chat",
        description = "Laugh along with GIM chat?"
    )
    fun gimChat(): Boolean {
        return true
    }
}
