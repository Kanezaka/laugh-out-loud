package com.example

import com.google.inject.Provides
import net.runelite.api.ChatMessageType
import net.runelite.api.Client
import net.runelite.api.GameState
import net.runelite.api.events.GameStateChanged
import net.runelite.client.config.ConfigManager
import net.runelite.client.eventbus.Subscribe
import net.runelite.client.plugins.Plugin
import net.runelite.client.plugins.PluginDescriptor
import org.apache.logging.log4j.kotlin.logger
import javax.inject.Inject

@PluginDescriptor(name = "Example")
class ExamplePlugin : Plugin() {

    companion object {
        private val LOGGER = logger()
    }

    @Inject
    lateinit var client: Client

    @Inject
    lateinit var config: ExampleConfig

    @Throws(Exception::class)
    override fun startUp() {
        LOGGER.info("$javaClass started!")
    }

    @Throws(Exception::class)
    override fun shutDown() {
        LOGGER.info("$javaClass stopped!")
    }

    @Subscribe
    fun onGameStateChanged(gameStateChanged: GameStateChanged) {
        if (gameStateChanged.gameState == GameState.LOGGED_IN) {
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null)
        }
    }

    @Provides
    fun provideConfig(configManager: ConfigManager): ExampleConfig {
        return configManager.getConfig(ExampleConfig::class.java)
    }
}
