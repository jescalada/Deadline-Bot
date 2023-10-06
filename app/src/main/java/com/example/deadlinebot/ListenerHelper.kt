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
    fun onMessageCreateEvent(kord: Kord, deadlines: MutableList<Deadline>) {
        kord.on<MessageCreateEvent> { // runs every time a message is created that our bot can read

            // ignore other bots, even ourselves. We only serve humans here!
            if (message.author?.isBot != false) return@on

            // check if our command is being invoked
            if (message.content[0] != '!') return@on

            // get the command (first word after the !)
            val split = message.content.split(" ")
            val command = split[0].substring(1)
            val args = split.drop(1)

            // interpret the command
            val response = CommandInterpreter.interpretCommand(command, args, deadlines)

            // all clear, give them the pong!
            message.channel.createMessage(response)
        }
    }






}