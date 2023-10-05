package com.example.deadlinebot

import dev.kord.core.Kord
import dev.kord.core.exception.KordInitializationException
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import io.github.cdimascio.dotenv.dotenv

suspend fun main() {
    // Get bot token from .env
    val dotenv = dotenv {
        directory = "app/assets"
        filename = "env"
    }
    val token = dotenv["BOT_TOKEN"]

    // Initialize bot
    val kord: Kord
    try {
        kord = Kord(token ?: "")
    } catch (e: KordInitializationException) {
        println("Invalid token. Please check your env file at app/assets/env.")
        return
    }

    // Register event listeners
    ListenerHelper.onMessageCreateEvent(kord)

    // Start bot (log it in)
    kord.login {
        // we need to specify this to receive the content of messages
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
    }
}

