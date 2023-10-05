package com.example.deadlinebot

import io.github.cdimascio.dotenv.dotenv
import kotlinx.datetime.LocalDateTime

object DataHelper {
    /**
     * Loads deadlines from a file.
     *
     * @param filename the name of the file to load from
     */
    fun loadDeadlinesFromFile(filename: String): MutableList<Deadline> {
        val deadlines = mutableListOf<Deadline>()
        val file = java.io.File(filename)

        file.forEachLine {
            val deadline = it.split(", ")
            deadlines.add(Deadline(deadline[0], deadline[1], LocalDateTime.parse(deadline[2])))
        }
        return deadlines
    }

    /**
     * Loads the bot token from the .env file.
     *
     * @return the bot token if available, otherwise an empty string
     */
    fun loadBotTokenFromEnv(directory: String, filename: String): String {
        // Get bot token from .env
        val dotenv = dotenv {
            this.directory = directory
            this.filename = filename
        }
        return dotenv["BOT_TOKEN"] ?: ""
    }
}