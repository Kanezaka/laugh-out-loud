package com.example

import net.runelite.client.RuneLite
import net.runelite.client.externalplugins.ExternalPluginManager

object ExamplePluginTest {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        ExternalPluginManager.loadBuiltin(ExamplePlugin::class.java)
        RuneLite.main(args)
    }
}