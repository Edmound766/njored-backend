package com.njored.domain

import com.njored.database.enums.MessageDirection
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class Conversation(
    val id: Uuid,
    val leadId: Uuid,
    val messageExternalId: String?,
    val direction: MessageDirection,
    val messageText: String,
)