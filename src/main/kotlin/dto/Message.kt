package com.njored.dto

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String,
    val from: String,
    val timestamp: String,
    val type: String,
    val text: TextMessage? = null
)