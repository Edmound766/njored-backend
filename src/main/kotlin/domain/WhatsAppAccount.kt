package com.njored.domain

import kotlinx.serialization.Serializable
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class WhatsAppAccount(
    val id: Uuid,
    val businessId: Uuid,
    val phoneNumberId: String,
    val businessAccountId: String,
    val accessToken: String,
    val webhookVerifyToken: String,
)