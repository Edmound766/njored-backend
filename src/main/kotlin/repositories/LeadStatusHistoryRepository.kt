package com.njored.repositories

import com.njored.database.DatabaseFactory.dbQuery
import com.njored.database.enums.LeadStatus
import com.njored.database.tables.LeadStatusHistoryTable
import com.njored.domain.LeadStatusHistory
import com.njored.repositories.mappers.toLeadStatusHistory
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insertReturning
import org.jetbrains.exposed.v1.jdbc.selectAll
import kotlin.uuid.Uuid

interface LeadStatusHistoryRepository {
    suspend fun record(
        leadId: Uuid,
        oldStatus: LeadStatus?,
        newStatus: LeadStatus,
        reason: String? = null
    ): LeadStatusHistory

    suspend fun findByLeadId(leadId: Uuid): List<LeadStatusHistory>
}

class LeadStatusHistoryRepositoryImpl : LeadStatusHistoryRepository {

    override suspend fun record(
        leadId: Uuid,
        oldStatus: LeadStatus?,
        newStatus: LeadStatus,
        reason: String?
    ): LeadStatusHistory = dbQuery {
        LeadStatusHistoryTable.insertReturning {
            it[LeadStatusHistoryTable.leadId] = leadId
            it[LeadStatusHistoryTable.oldStatus] = oldStatus
            it[LeadStatusHistoryTable.newStatus] = newStatus
            it[LeadStatusHistoryTable.reason] = reason
        }.single().toLeadStatusHistory()
    }

    override suspend fun findByLeadId(leadId: Uuid): List<LeadStatusHistory> = dbQuery {
        LeadStatusHistoryTable
            .selectAll()
            .where { LeadStatusHistoryTable.leadId eq leadId }
            .map { it.toLeadStatusHistory() }
    }
}