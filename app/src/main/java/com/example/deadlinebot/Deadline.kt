package com.example.deadlinebot

import kotlinx.datetime.LocalDateTime

/**
 * Data class for a deadline.
 */
data class Deadline(
    val name: String,
    val description: String,
    val date: LocalDateTime
)