package com.njored.database.tables

import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestampWithTimeZone
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object WhatsAppAccountsTable : UuidTable("whatsapp_accounts") {
    val businessId = reference("business_id", BusinessesTable)
    val phoneNumber = varchar("phone_number_id", 255)
    val businessAccountId = varchar("business_account_id", 255)
    val accessToken = text("access_token")
    val webhookVerifyToken = varchar("webhook_verify_token", 255)
    val createdAt = timestampWithTimeZone("created_at").defaultExpression(CurrentTimestampWithTimeZone)
}