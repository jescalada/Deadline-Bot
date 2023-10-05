package com.example.deadlinebot

import dev.kord.core.Kord
import dev.kord.core.exception.KordInitializationException
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent

suspend fun main() {
    val token = DataHelper.loadBotTokenFromEnv("app/assets", "env")

    // Initialize bot
    val kord: Kord
    try {
        kord = Kord(token)
    } catch (e: KordInitializationException) {
        println("Invalid token. Please check your env file at app/assets/env.")
        return
    }

    val deadlines = DataHelper.loadDeadlinesFromFile("app/assets/deadlines.txt")

    // Register event listeners
    ListenerHelper.onMessageCreateEvent(kord, deadlines)

    // Start bot (log it in)
    kord.login {
        // we need to specify this to receive the content of messages
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
    }
}

