package com.kanezaka

import com.google.inject.Provides
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import net.runelite.api.ChatMessageType
import net.runelite.api.events.ChatMessage
import net.runelite.client.config.ConfigManager
import net.runelite.client.eventbus.Subscribe
import net.runelite.client.plugins.Plugin
import net.runelite.client.plugins.PluginDescriptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.util.*
import javax.inject.Inject
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineEvent

@PluginDescriptor(
    name = "Laugh Out Loud",
    description = "Enjoy others' joy. Plays audio when others laugh.",
    tags = ["audio", "sound", "lol", "laugh"]
)
class LaughOutLoudPlugin : Plugin() {
    companion object {
        val log: Logger = LoggerFactory.getLogger(LaughOutLoudPlugin::class.java)
        private val CHUCKLE_LIST = sortedSetOf(
            "lmao",
            "rofl",
            "roflmao",
        )
        private val HA_REGEX = Regex("^([hH]|[aA])([hH]|[aA])+$")
        private val LOL_REGEX = Regex("^[lL]([oO0]|[lL])+$")
        // I used a semaphore since there may be a world where we want more than one
        // laugh to play at the same time.
        private val playSoundSemaphore = Semaphore(1)
        private val scope = CoroutineScope(Dispatchers.Default)
        private val VALID_CHAT_TYPES = sortedSetOf(
            ChatMessageType.PUBLICCHAT,
            ChatMessageType.PRIVATECHAT,
            ChatMessageType.CLAN_CHAT,
            ChatMessageType.FRIENDSCHAT,
            ChatMessageType.CLAN_GIM_CHAT
        )
    }

    @Inject
    lateinit var config: LaughOutLoudConfig

    @Throws(Exception::class)
    override fun startUp() {
        log.info("${javaClass.simpleName} started!")
    }

    @Throws(Exception::class)
    override fun shutDown() {
        log.info("${javaClass.simpleName} stopped!")
    }

    @Subscribe
    fun onChatMessage(chatMessage: ChatMessage) {
        if (isValidType(chatMessage.type) && isCandidateMessage(chatMessage.message)) {
            playSoundAsync()
        }
    }

    private fun isValidType(type: ChatMessageType): Boolean {
        val validType = VALID_CHAT_TYPES.contains(type)
        val unfiltered = when (type) {
            ChatMessageType.PUBLICCHAT -> config.publicChat()
            ChatMessageType.PRIVATECHAT -> config.privateChat()
            ChatMessageType.CLAN_CHAT -> config.clanChat()
            ChatMessageType.FRIENDSCHAT -> config.friendsChat()
            ChatMessageType.CLAN_GIM_CHAT -> config.gimChat()
            else -> true
        }
        return validType && unfiltered
    }

    private fun isCandidateMessage(messageText: String): Boolean {
        val normalizedText = messageText.lowercase()
        return CHUCKLE_LIST.contains(normalizedText) ||
                HA_REGEX.matches(normalizedText) ||
                LOL_REGEX.matches(normalizedText) ||
                config.chuckles()?.split(",")?.contains(normalizedText) ?: false
    }

    private fun playSoundAsync() {
        scope.launch {
            playSoundWithLimit()
        }
    }

    private suspend fun playSoundWithLimit() {
        playSoundSemaphore.withPermit {
            val stream: InputStream = javaClass.getResourceAsStream("/sounds/mixkit-dwarf-creature-laugh-420.wav")
                ?: return
            val audioStream: AudioInputStream = AudioSystem.getAudioInputStream(stream)

            val clip: Clip = AudioSystem.getClip()
            val completion = CompletableDeferred<Unit>()

            clip.addLineListener { event ->
                if (event.type == LineEvent.Type.STOP) {
                    completion.complete(Unit)
                }
            }
            clip.open(audioStream)
            clip.start()

            completion.await()

            clip.close()
        }
    }

    @Provides
    fun provideConfig(configManager: ConfigManager): LaughOutLoudConfig {
        return configManager.getConfig(LaughOutLoudConfig::class.java)
    }
}