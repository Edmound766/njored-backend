package com.njored.domain

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import kotlin.uuid.Uuid

@Serializable
data class Agent(
    val id: Uuid,
    val name: String,
    val phoneNumber: String,
    val telegramChatId: String?,
    val isActive: Boolean,
    val businessId: Uuid?,
    @Contextual
    val lastAssignmentAt: OffsetDateTime?,
)