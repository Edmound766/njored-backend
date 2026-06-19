package com.njored.services

import com.njored.database.enums.MessageDirection
import com.njored.domain.Conversation
import com.njored.repositories.ConversationRepository
import kotlin.uuid.Uuid

interface ConversationService {
    suspend fun recordInboundMessage(leadId: Uuid, messageText: String): Conversation
    suspend fun recordOutboundMessage(leadId: Uuid, messageText: String, isBot: Boolean = false): Conversation
    suspend fun getConversationHistory(leadId: Uuid): List<Conversation>
}

class ConversationServiceImpl(
    private val conversationRepository: ConversationRepository
) : ConversationService {

    override suspend fun recordInboundMessage(leadId: Uuid, messageText: String): Conversation {
        return conversationRepository.create(
            leadId = leadId,
            direction = MessageDirection.INBOUND,
            messageText = messageText
        )
    }

    override suspend fun recordOutboundMessage(
        leadId: Uuid,
        messageText: String,
        isBot: Boolean
    ): Conversation {
        return conversationRepository.create(
            leadId = leadId,
            direction = if (isBot) MessageDirection.OUTBOUND_BOT else MessageDirection.OUTBOUND_HUMAN,
            messageText = messageText
        )
    }

    override suspend fun getConversationHistory(leadId: Uuid): List<Conversation> {
        return conversationRepository.findByLeadId(leadId)
    }
}