package com.njored.repositories.mappers

import com.njored.database.tables.LeadsTable
import com.njored.domain.Lead
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toLead(): Lead = Lead(
    id = this[LeadsTable.id].value,
    externalId = this[LeadsTable.externalId],
    channel = this[LeadsTable.channel],
    name = this[LeadsTable.name],
    status = this[LeadsTable.status],
    assignedAgentId = this[LeadsTable.assignedAgentId]?.value,
    businessId = this[LeadsTable.businessId]?.value,
)