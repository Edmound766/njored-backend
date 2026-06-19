package com.njored.domain

import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class Business(
    val id: Uuid,
    val name: String,
    val contactEmail: String?,
)