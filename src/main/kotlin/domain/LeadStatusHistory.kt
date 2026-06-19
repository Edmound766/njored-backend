package com.njored.domain

import com.njored.database.enums.LeadStatus
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class LeadStatusHistory(
    val id: Uuid,
    val leadId: Uuid,
    val oldStatus: LeadStatus?,
    val newStatus: LeadStatus,
    val reason: String?,
)