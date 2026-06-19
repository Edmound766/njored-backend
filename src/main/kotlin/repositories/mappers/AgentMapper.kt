package com.njored.repositories.mappers

import com.njored.database.tables.AgentsTable
import com.njored.domain.Agent
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toAgent(): Agent = Agent(
    id = this[AgentsTable.id].value,
    name = this[AgentsTable.name],
    phoneNumber = this[AgentsTable.phoneNumber],
    telegramChatId = this[AgentsTable.telegramChatId],
    isActive = this[AgentsTable.isActive],
    businessId = this[AgentsTable.businessId]?.value,
    lastAssignmentAt = this[AgentsTable.lastAssignmentAt],
)