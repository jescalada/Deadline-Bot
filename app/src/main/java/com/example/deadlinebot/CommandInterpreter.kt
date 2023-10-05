package com.example.deadlinebot

import kotlinx.datetime.LocalDateTime

/**
 * Singleton for interpreting commands.
 */
object CommandInterpreter {
    /**
     * Interprets a command and returns a response.
     *
     * @param command The command to interpret.
     * @return The response to the command.
     */
    fun interpretCommand(command: String, args: List<String>, deadlines: MutableList<Deadline>): String {
        return when (command) {
            "add" -> addDeadline(args, deadlines)
            "remove" -> "I'm here to remove!"
            "deadlines" -> deadlines(deadlines)
            "settings" -> "I'm here to change settings!"
            "help" -> help(args)
            else -> "I don't know that command."
        }
    }

    private fun deadlines(deadlines: MutableList<Deadline>): String {
         return "**Upcoming Deadlines:**\n" +
                 deadlines.joinToString("\n----------\n") { "**${it.name}** - ${it.date.dayOfWeek.toString().substring(0, 3)}, ${it.date.month.name} ${it.date.dayOfMonth} ${ it.date.time}\n${it.description}" }
    }


    private fun addDeadline(args: List<String>, deadlines: MutableList<Deadline>): String {
        return try {
            deadlines.add(Deadline(args[0], args[1], LocalDateTime.parse(args[2])))
            "Deadline ${args[0]} added!"
        } catch (e: Exception) {
            "Invalid arguments."
        }
    }

    /**
     * Handles the help command. Provides help for other available commands.
     *
     * @param args The arguments to the help command.
     * @return The response to the help command.
     */
    private fun help(args: List<String>): String {
        if (args.isEmpty()) {
            return "Available commands: !add, !remove, !deadlines, !settings, !help.\n" +
                    "Use !help <command> to get more info about a command."
        }
        return when (args[0]) {
            "add" -> "Adds a deadline. Usage: !add <name> <date> <description>"
            "remove" -> "Removes a deadline. Usage: !remove <name>"
            "deadlines" -> "Shows all deadlines. Usage: !deadlines"
            "settings" -> "Changes settings. Usage: !settings <setting> <value>"
            "help" -> "Shows help. Usage: !help <command>"
            else -> "I don't know that command."
        }
    }
}