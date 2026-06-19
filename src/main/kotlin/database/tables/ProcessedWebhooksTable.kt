package com.njored.database.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.CurrentTimestampWithTimeZone
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone


object ProcessedWebhooksTable : Table("processed_webhooks") {
    val webhookId = varchar("webhook_id", 255)
    val processedAt = timestampWithTimeZone("processed_at").defaultExpression(CurrentTimestampWithTimeZone)

    override val primaryKey = PrimaryKey(webhookId)
}