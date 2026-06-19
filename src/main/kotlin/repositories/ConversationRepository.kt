package com.njored.repositories

import com.njored.database.DatabaseFactory.dbQuery
import com.njored.database.enums.MessageDirection
import com.njored.database.tables.ConversationsTable
import com.njored.domain.Conversation
import com.njored.repositories.mappers.toConversation
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insertReturning
import org.jetbrains.exposed.v1.jdbc.selectAll
import kotlin.uuid.Uuid

interface ConversationRepository {
    suspend fun create(
        leadId: Uuid,
        direction: MessageDirection,
        messageText: String,
        messageExternalId: String? = null
    ): Conversation

    suspend fun findByLeadId(leadId: Uuid): List<Conversation>
}

class ConversationRepositoryImpl : ConversationRepository {

    override suspend fun create(
        leadId: Uuid,
        direction: MessageDirection,
        messageText: String,
        messageExternalId: String?
    ): Conversation = dbQuery {
        ConversationsTable.insertReturning {
            it[ConversationsTable.leadId] = leadId
            it[ConversationsTable.direction] = direction
            it[ConversationsTable.messageText] = messageText
            it[ConversationsTable.messageExternalId] = messageExternalId
        }.single().toConversation()
    }

    override suspend fun findByLeadId(leadId: Uuid): List<Conversation> = dbQuery {
        ConversationsTable
            .selectAll()
            .where { ConversationsTable.leadId eq leadId }
            .map { it.toConversation() }
    }
}