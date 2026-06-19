package com.njored.dto

import kotlinx.serialization.Serializable

@Serializable
data class TextMessage(
    val body: String
)