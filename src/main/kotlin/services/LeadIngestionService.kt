package com.njored.services

import com.njored.domain.Lead
import com.njored.repositories.LeadRepository
import kotlin.uuid.Uuid

class LeadIngestionService(
    private val leadRepository: LeadRepository,
    private val conversationService: ConversationService,
    private val assignmentService: AssignmentService,
    private val whatsAppClient: WhatsAppClient
) {

    suspend fun ingestInboundMessage(
        businessId: Uuid,
        phoneNumber: String,
        leadName: String?,
        messageText: String,
        phoneNumberId: String,
        accessToken: String
    ): Lead {

        var lead = leadRepository.findByExternalId(businessId, phoneNumber)

        if (lead == null) {

            lead = leadRepository.create(
                businessId,
                externalId = phoneNumber,
                name = leadName
            )

            val assignedAgent = assignmentService.assignLead(businessId, lead.id)

            println(
                "🎯 New Lead assigned: ${
                    lead.name ?: phoneNumber
                } -> ${assignedAgent.name}"
            )

            whatsAppClient.sendTextMessage(
                phoneNumberId = phoneNumberId,
                accessToken = accessToken,
                to = phoneNumber,
                text = "Hello${
                    if (!leadName.isNullOrBlank()) " ${leadName.split(" ").first()}" else ""
                }! 👋 Thank you for reaching out. One of our agents will be with you shortly."
            )

        } else {

            println(
                "💬 Ongoing Conversation: ${
                    lead.name ?: phoneNumber
                }"
            )
        }

        conversationService.recordInboundMessage(
            lead.id,
            messageText
        )

        return lead
    }
}