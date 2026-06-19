package com.njored.repositories

import com.njored.database.DatabaseFactory.dbQuery
import com.njored.database.tables.AgentsTable
import com.njored.domain.Agent
import com.njored.repositories.mappers.toAgent
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insertReturning
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import java.time.OffsetDateTime
import kotlin.uuid.Uuid

interface AgentRepository {
    suspend fun create(
        name: String,
        phoneNumber: String,
        businessId: Uuid?,
        telegramChatId: String? = null
    ): Agent

    suspend fun findActiveAgents(businessId: Uuid): List<Agent>
    suspend fun updateLastAssignment(agentId: Uuid)
    suspend fun findById(id: Uuid): Agent?
}

class AgentRepositoryImpl : AgentRepository {

    override suspend fun create(
        name: String,
        phoneNumber: String,
        businessId: Uuid?,
        telegramChatId: String?
    ): Agent = dbQuery {
        AgentsTable.insertReturning {
            it[AgentsTable.name] = name
            it[AgentsTable.phoneNumber] = phoneNumber
            it[AgentsTable.businessId] = businessId
            it[AgentsTable.telegramChatId] = telegramChatId
        }.single().toAgent()
    }

    override suspend fun findActiveAgents(businessId: Uuid): List<Agent> = dbQuery {
        AgentsTable
            .selectAll()
            .where {
                (AgentsTable.businessId eq businessId) and
                        (AgentsTable.isActive eq true)
            }
            .map { it.toAgent() }
    }

    override suspend fun updateLastAssignment(agentId: Uuid) {
        dbQuery {
            AgentsTable.update({ AgentsTable.id eq agentId }) {
                it[lastAssignmentAt] = OffsetDateTime.now()
            }
        }
    }

    override suspend fun findById(id: Uuid): Agent? = dbQuery {
        AgentsTable
            .selectAll()
            .where { AgentsTable.id eq id }
            .singleOrNull()
            ?.toAgent()
    }
}