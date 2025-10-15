package com.example.assignment3.domain.model

import java.util.UUID

data class MoodEntry(
    val id: String = UUID.randomUUID().toString(),
    val timestampMillis: Long,
    val emoji: String,
    val note: String = ""
)
