package com.njored.database.tables

import com.njored.database.enums.ChannelType
import com.njored.database.enums.LeadStatus
import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.CurrentTimestampWithTimeZone
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone
import org.jetbrains.exposed.v1.json.json

object LeadsTable : UuidTable("leads") {
    val externalId = varchar("external_id", 100)
    val channel = customEnumeration(
        name = "channel",
        sql = "channel_type",
        fromDb = { value -> ChannelType.valueOf(value as String) },
        toDb = { value -> value.name }
    ).default(ChannelType.WHATSAPP)
    val name = varchar("name", 255).nullable()
    val status = customEnumeration(
        name = "status",
        sql = "lead_status",
        fromDb = { value -> LeadStatus.valueOf(value as String) },
        toDb = { value -> value.name }
    ).default(LeadStatus.BOT_QUALIFYING)
    val assignedAgentId = reference("assigned_agent_id", AgentsTable).nullable()
    val businessId = reference("business_id", BusinessesTable).nullable()
    val metadata = json<JsonObject>("metadata", Json.Default)
        .default(JsonObject(emptyMap()))
    val lastInteractionAt = timestampWithTimeZone("last_interaction_at").defaultExpression(CurrentTimestampWithTimeZone)
    val createdAt = timestampWithTimeZone("created_at").defaultExpression(CurrentTimestampWithTimeZone)

    init {
        uniqueIndex(businessId, externalId)
    }
}