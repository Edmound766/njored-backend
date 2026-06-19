package com.njored.dto

import kotlinx.serialization.Serializable

@Serializable
data class Change(
    val value: Value,
    val field: String
)