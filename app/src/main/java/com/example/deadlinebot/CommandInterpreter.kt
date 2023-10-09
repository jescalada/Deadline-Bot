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
    fun interpretCommand(command: String, args: List<String>, status: Status): String {
        return when (command) {
            "add" -> addDeadline(args, status.deadlines)
            "remove" -> removeDeadline(args, status.deadlines)
            "deadlines" -> deadlines(status.deadlines)
            "settings" -> settings(args, status.settings)
            "help" -> help(args)
            else -> "I don't know that command. Say !help for a list of available commands."
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
            "**Error**: Something went wrong. Example usage: !add <name>, <description>, <date and time>:\n" +
                    "_!add Quiz #1, This is my test quiz, 2023/10/11 16:30_"
        }
    }

    /**
     * Handles the remove command. Removes a deadline from the list of deadlines.
     *
     * @param args The arguments to the remove command.
     * @return The response to the remove command.
     */
    private fun removeDeadline(args: List<String>, deadlines: MutableList<Deadline>): String {
        return try {
            val fullName = args.joinToString(separator = " ") { it }
            val deadline = deadlines.find { it.name == fullName }
            deadlines.remove(deadline)
            "Removed **${fullName}**!"
        } catch (e: IndexOutOfBoundsException) {
            println(e)
            "**Error**: Invalid arguments."
        } catch (e: Exception) {
            println(e)
            "**Error**: Something went wrong. Example usage: !remove <name>:\n" +
                    "_!remove Quiz #1_"
        }
    }

    /**
     * Handles the settings command. Allows viewing and changing settings.
     *
     * @param args The arguments to the settings command.
     * @return The response to the settings command.
     */
    private fun settings(args: List<String>, settings: MutableMap<String, String>): String {
        return try {
            if (args.isEmpty()) {
                // Return all key value setting pairs
                return "## Settings" + settings.map { "\n${it.key}: ${it.value}" }.joinToString("")
            }
            when (args[0]) {
                "interval" -> {
                    if (args.size <= 1) {
                        return "Current reminder interval: **${settings["interval"]}** hours before."
                    }
                    val joined = args.drop(1).joinToString(separator = " ") { it }
                    val intervals = joined.split(",").map { it.trim().toInt() }

                    val intervalsParsed = intervals.toString().replace("[", "").replace("]", "")

                    settings["interval"] = intervalsParsed
                    "**Reminder interval** was set to **$intervalsParsed**."
                }
                "autoremind" -> {
                    if (args.size <= 1) {
                        return "Current autoremind setting: **${settings["autoremind"]}**."
                    }
                    val joined = args.drop(1).joinToString(separator = " ") { it }
                    val autoremind = joined.toBoolean()

                    settings["autoremind"] = autoremind.toString()
                    "**Autoremind** was set to **$autoremind**."
                }
                else -> "I don't know that setting."
            }
        } catch (e: IndexOutOfBoundsException) {
            println(e)
            "**Error**: Invalid arguments."
        } catch (e: Exception) {
            println(e)
            "**Error**: Something went wrong. Example usage: !settings <setting> <value>:\n" +
                    "_!settings interval 72, 24_"
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