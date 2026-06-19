package com.njored.database.tables

import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.CurrentTimestampWithTimeZone
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object AgentsTable : UuidTable("agents") {
    val name = varchar("name", 255)
    val phoneNumber = varchar("phone_number", 50).uniqueIndex()
    val telegramChatId = varchar("telegram_chat_id", 100).nullable()
    val isActive = bool("is_active").default(true)
    val businessId = reference("business_id", BusinessesTable).nullable()
    val lastAssignmentAt = timestampWithTimeZone("last_assignment_at").nullable()
    val createdAt = timestampWithTimeZone("created_at").defaultExpression(
        CurrentTimestampWithTimeZone
    )
}