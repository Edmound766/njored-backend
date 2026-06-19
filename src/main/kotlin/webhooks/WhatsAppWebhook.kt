package com.njored.webhooks

import com.njored.dto.WhatsAppWebhookPayload
import com.njored.repositories.ProcessedWebhookRepository
import com.njored.repositories.WhatsAppAccountRepository
import com.njored.services.LeadIngestionService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.whatsAppWebhook(
    leadIngestionService: LeadIngestionService,
    whatsappAccountRepository: WhatsAppAccountRepository,
    processedWebhookRepository: ProcessedWebhookRepository
) {
    get("/webhooks/whatsapp") {
        val mode = call.request.queryParameters["hub.mode"]
        val verifyToken = call.request.queryParameters["hub.verify_token"]
        val challenge = call.request.queryParameters["hub.challenge"]

        if (mode != "subscribe" || verifyToken == null || challenge == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }

        val account = whatsappAccountRepository.findByVerifyToken(verifyToken)

        if (account == null) {
            call.respond(HttpStatusCode.Forbidden)
            return@get
        }

        call.respondText(challenge)
    }

    post("/webhooks/whatsapp") {
        val payload = try {
            call.receive<WhatsAppWebhookPayload>()
        } catch (e: Exception) {
            println("⚠️ Failed to parse webhook payload: ${e.message}")
            call.respond(HttpStatusCode.OK)
            return@post
        }

        // Always respond 200 immediately — Meta retries on slow or non-200 responses
        call.respond(HttpStatusCode.OK)

        val change = payload.entry
            .firstOrNull()
            ?.changes
            ?.firstOrNull()
            ?: return@post

        val value = change.value
        val contact = value.contacts.firstOrNull() ?: return@post
        val message = value.messages.firstOrNull() ?: return@post

        // Deduplication — Meta can deliver the same webhook more than once
        if (processedWebhookRepository.exists(message.id)) {
            println("⚠️ Duplicate webhook ignored: ${message.id}")
            return@post
        }
        processedWebhookRepository.save(message.id)

        println("📲 Phone Number ID: ${value.metadata.phoneNumberId}")
        println("👤 Sender Name: ${contact.profile.name}")
        println("📞 Sender Phone: ${contact.waId}")
        println("💬 Message: ${message.text?.body}")

        val account = whatsappAccountRepository.findByPhoneNumberId(
            value.metadata.phoneNumberId
        )

        if (account == null) {
            println("⚠️ No WhatsApp account configured for phone number id: ${value.metadata.phoneNumberId}")
            return@post
        }

        leadIngestionService.ingestInboundMessage(
            businessId = account.businessId,
            phoneNumber = contact.waId,
            leadName = contact.profile.name,
            messageText = message.text?.body ?: "",
            phoneNumberId = account.phoneNumberId,
            accessToken = account.accessToken
        )
    }
}