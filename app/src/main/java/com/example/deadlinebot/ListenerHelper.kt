package com.example.deadlinebot

import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on

object ListenerHelper {

    /**
     * This function is called when the bot is ready to start receiving events.
     *
     * @param kord The Kord instance that is ready to start receiving events.
     */
    fun onMessageCreateEvent(kord: Kord): Unit {
        kord.on<MessageCreateEvent> { // runs every time a message is created that our bot can read

            // ignore other bots, even ourselves. We only serve humans here!
            if (message.author?.isBot != false) return@on

            // check if our command is being invoked
            if (message.content[0] != '!') return@on

            // get the command
            val command = message.content.substring(1)

            // interpret the command
            val response = interpretCommand(command)

            // all clear, give them the pong!
            message.channel.createMessage(response)
        }
    }

    private fun interpretCommand(command: String): String {
        return when (command) {
            "ping" -> "pong!"
            "help" -> "I'm here to help!"
            else -> "I don't know that command."
        }
    }


}