package com.njored.repositories.mappers

import com.njored.database.tables.ConversationsTable
import com.njored.domain.Conversation
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toConversation(): Conversation = Conversation(
    id = this[ConversationsTable.id].value,
    leadId = this[ConversationsTable.leadId].value,
    messageExternalId = this[ConversationsTable.messageExternalId],
    direction = this[ConversationsTable.direction],
    messageText = this[ConversationsTable.messageText],
)