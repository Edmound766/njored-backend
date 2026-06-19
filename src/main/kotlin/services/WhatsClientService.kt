package com.njored.services

import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

interface WhatsAppClient {
    suspend fun sendTextMessage(
        phoneNumberId: String,
        accessToken: String,
        to: String,
        text: String
    )
}

class MetaWhatsAppClient(private val httpClient: HttpClient) : WhatsAppClient {

    override suspend fun sendTextMessage(
        phoneNumberId: String,
        accessToken: String,
        to: String,
        text: String
    ) {
        httpClient.post(
            "https://graph.facebook.com/v19.0/$phoneNumberId/messages"
        ) {
            bearerAuth(accessToken)
            contentType(ContentType.Application.Json)
            setBody(
                OutboundTextMessage(
                    to = to,
                    text = OutboundText(body = text)
                )
            )
        }
    }
}

@Serializable
private data class OutboundTextMessage(
    val messaging_product: String = "whatsapp",
    val recipient_type: String = "individual",
    val to: String,
    val type: String = "text",
    val text: OutboundText
)

@Serializable
private data class OutboundText(
    val preview_url: Boolean = false,
    val body: String
)