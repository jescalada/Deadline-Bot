package com.example.deadlinebot

import kotlinx.datetime.LocalDateTime
import java.time.format.DateTimeParseException

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

    /**
     * Handles the deadlines command. Returns a list of all deadlines.
     *
     * @return The response to the deadlines command.
     * @param deadlines The list of deadlines.
     */
    private fun deadlines(deadlines: MutableList<Deadline>): String {
         return "## Upcoming Deadlines:\n" +
                 deadlines.joinToString("\n----------\n") {
                     val monthName = it.date.month.name
                     val month = monthName.substring(0, 1) + monthName.substring(1).lowercase()

                     val dayOfWeekName = it.date.dayOfWeek.toString()
                     val dayOfWeek = dayOfWeekName.substring(0, 1) + dayOfWeekName.substring(1).lowercase()
                     "**${it.name}** - ${dayOfWeek.substring(0, 3)}, $month ${it.date.dayOfMonth} ${it.date.time}\n" +
                             it.description
                 }
    }

    /**
     * Handles the add command. Adds a deadline to the list of deadlines.
     *
     * @param args The arguments to the add command.
     * @return The response to the add command.
     */
    private fun addDeadline(args: List<String>, deadlines: MutableList<Deadline>): String {
        return try {
            val joined = args.joinToString(separator = " ") { it }
            val deadlineValues = joined.split(", ")

            // todo: convert to UTC
            val dateFormatted = deadlineValues[2].replace(" ", "T").replace("/", "-")

            deadlines.add(Deadline(deadlineValues[0], deadlineValues[1], LocalDateTime.parse(dateFormatted)))
            "Added **${args[0]}**!"
        } catch (e: IndexOutOfBoundsException) {
            println(e)
            "**Error**: Invalid arguments."
        } catch (e: DateTimeParseException) {
            println(e)
            "**Error**: Invalid date. The required format is: YYYY-MM-DD HH:mm"
        } catch (e: Exception) {
            println(e)
            "**Error**: Something went wrong. Example usage <name>, <description>, <date and time>:\n" +
                    "_!add Quiz #1, This is my test quiz, 2023/10/11 16:30_"
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