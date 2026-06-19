package com.njored.dto
import kotlinx.serialization.Serializable

@Serializable
data class WhatsAppWebhookPayload(
    val `object`: String,
    val entry: List<Entry>
)