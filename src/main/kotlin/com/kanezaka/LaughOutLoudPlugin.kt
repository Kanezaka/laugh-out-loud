package com.kanezaka

import com.google.inject.Provides
import net.runelite.api.Client
import net.runelite.api.events.ChatMessage
import net.runelite.client.config.ConfigManager
import net.runelite.client.eventbus.Subscribe
import net.runelite.client.plugins.Plugin
import net.runelite.client.plugins.PluginDescriptor
import org.apache.logging.log4j.kotlin.logger
import javax.inject.Inject

@PluginDescriptor(
    name = "Laugh Out Loud",
    description = "Enjoy others' joy. Plays audio when others laugh.",
    tags = ["audio", "sound", "lol", "laugh"]
)
class LaughOutLoudPlugin : Plugin() {
    companion object {
        private val LOGGER = logger()
        private val CHUCKLE_LIST = sortedSetOf(
            "lmao",
            "rofl",
            "roflmao",
        )
        private val HA_REGEX = Regex("^([hH]|[aA])([hH]|[aA])+$")
        private val LOL_REGEX = Regex("^[lL]([oO0]|[lL])+$")
    }

    @Inject
    lateinit var client: Client

    @Inject
    lateinit var config: LaughOutLoudConfig

    @Throws(Exception::class)
    override fun startUp() {
        LOGGER.info("$javaClass started!")
    }

    @Throws(Exception::class)
    override fun shutDown() {
        LOGGER.info("$javaClass stopped!")
    }

    @Subscribe
    fun onChatMessage(chatMessage: ChatMessage) {
        if (isCandidateMessage(chatMessage.message)) {
            println("Message detected")
        }
    }

    @Provides
    fun provideConfig(configManager: ConfigManager): LaughOutLoudConfig {
        return configManager.getConfig(LaughOutLoudConfig::class.java)
    }

    private fun isCandidateMessage(messageText: String): Boolean {
        val normalizedText = messageText.lowercase()
        return CHUCKLE_LIST.contains(normalizedText) ||
                HA_REGEX.matches(normalizedText) ||
                LOL_REGEX.matches(normalizedText)
    }
}
