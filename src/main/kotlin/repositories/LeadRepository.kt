package com.njored.repositories

import com.njored.database.DatabaseFactory.dbQuery
import com.njored.database.enums.LeadStatus
import com.njored.database.tables.LeadsTable
import com.njored.domain.Lead
import com.njored.repositories.mappers.toLead
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insertReturning
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import kotlin.uuid.Uuid

interface LeadRepository {
    suspend fun create(businessId: Uuid, externalId: String, name: String?): Lead
    suspend fun findById(id: Uuid): Lead?
    suspend fun findByExternalId(businessId: Uuid, externalId: String): Lead?
    suspend fun assignAgent(leadId: Uuid, agentId: Uuid)
    suspend fun updateStatus(leadId: Uuid, status: LeadStatus)
}

class LeadRepositoryImpl : LeadRepository {

    override suspend fun create(
        businessId: Uuid,
        externalId: String,
        name: String?
    ): Lead = dbQuery {
        LeadsTable.insertReturning {
            it[LeadsTable.businessId] = businessId
            it[LeadsTable.externalId] = externalId
            it[LeadsTable.name] = name
        }.single().toLead()
    }

    override suspend fun findById(id: Uuid): Lead? = dbQuery {
        LeadsTable
            .selectAll()
            .where { LeadsTable.id eq id }
            .singleOrNull()
            ?.toLead()
    }

    override suspend fun findByExternalId(businessId: Uuid, externalId: String): Lead? = dbQuery {
        LeadsTable
            .selectAll()
            .where {
                (LeadsTable.businessId eq businessId) and
                        (LeadsTable.externalId eq externalId)
            }
            .singleOrNull()
            ?.toLead()
    }

    override suspend fun assignAgent(leadId: Uuid, agentId: Uuid) {
        dbQuery {
            LeadsTable.update({ LeadsTable.id eq leadId }) {
                it[assignedAgentId] = agentId
            }
        }
    }

    override suspend fun updateStatus(leadId: Uuid, status: LeadStatus) {
        dbQuery {
            LeadsTable.update({ LeadsTable.id eq leadId }) {
                it[LeadsTable.status] = status
            }
        }
    }
}