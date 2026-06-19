package com.njored.database.tables

import com.njored.database.enums.MessageDirection
import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.jetbrains.exposed.v1.datetime.CurrentTimestampWithTimeZone
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone
import org.jetbrains.exposed.v1.json.json

object ConversationsTable : UuidTable("conversations") {
    val leadId = reference("lead_id", LeadsTable)
    val messageExternalId = varchar("message_external_id", 255).nullable().uniqueIndex()
    val direction = customEnumeration(
        name = "direction",
        sql = "message_direction",
        fromDb = { value -> MessageDirection.valueOf(value as String) },
        toDb = { value -> value.name }
    )
    val messageText = text("message_text")
    val rawPayload = json<JsonObject>("raw_payload", Json.Default)
        .default(JsonObject(emptyMap()))
    val createdAt = timestampWithTimeZone("created_at").defaultExpression(CurrentTimestampWithTimeZone)
}