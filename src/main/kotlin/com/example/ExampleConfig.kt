package com.example

import net.runelite.client.config.Config
import net.runelite.client.config.ConfigGroup
import net.runelite.client.config.ConfigItem

@ConfigGroup("example")
interface ExampleConfig : Config {
    @ConfigItem(
        keyName = "greeting",
        name = "Welcome Greeting",
        description = "The message to show to the user when they login"
    )
    fun greeting(): String {
        return "Hello"
    }
}
