package com.njored.dto

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class LeadIngestRequest(
    val businessId: Uuid,
    val phoneNumber: String,
    val leadName: String? = null,
    val messageText: String
)