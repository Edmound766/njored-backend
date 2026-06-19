package com.njored.repositories.mappers

import com.njored.database.tables.LeadStatusHistoryTable
import com.njored.domain.LeadStatusHistory
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toLeadStatusHistory(): LeadStatusHistory = LeadStatusHistory(
    id = this[LeadStatusHistoryTable.id].value,
    leadId = this[LeadStatusHistoryTable.leadId].value,
    oldStatus = this[LeadStatusHistoryTable.oldStatus],
    newStatus = this[LeadStatusHistoryTable.newStatus],
    reason = this[LeadStatusHistoryTable.reason],
)