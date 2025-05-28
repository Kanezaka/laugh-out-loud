package com.kanezaka

import net.runelite.client.RuneLite
import net.runelite.client.externalplugins.ExternalPluginManager

object LaughOutLoudPluginTest {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        ExternalPluginManager.loadBuiltin(LaughOutLoudPlugin::class.java)
        RuneLite.main(args)
    }
}