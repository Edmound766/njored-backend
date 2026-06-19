package com.njored.domain

import com.njored.database.enums.ChannelType
import com.njored.database.enums.LeadStatus
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class Lead(
    val id: Uuid,
    val externalId: String,
    val channel: ChannelType,
    val name: String?,
    val status: LeadStatus,
    val assignedAgentId: Uuid?,
    val businessId: Uuid?,
)