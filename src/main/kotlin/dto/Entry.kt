package com.njored.dto

import kotlinx.serialization.Serializable

@Serializable
data class Entry(
    val id: String,
    val changes: List<Change>
)