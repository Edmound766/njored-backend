package com.njored.database.tables

import com.njored.database.enums.LeadStatus
import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.CurrentTimestampWithTimeZone
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone


object LeadStatusHistoryTable : UuidTable("lead_status_history") {
    val leadId = reference("lead_id", LeadsTable)
    val oldStatus = customEnumeration(
        name = "old_status",
        sql = "lead_status",
        fromDb = { value -> LeadStatus.valueOf(value as String) },
        toDb = { value -> value.name }
    ).nullable()

    val newStatus = customEnumeration(
        name = "new_status",
        sql = "lead_status",
        fromDb = { value -> LeadStatus.valueOf(value as String) },
        toDb = { value -> value.name }
    )
    val reason = text("reason").nullable()
    val createdAt = timestampWithTimeZone("created_at").defaultExpression(CurrentTimestampWithTimeZone)
}