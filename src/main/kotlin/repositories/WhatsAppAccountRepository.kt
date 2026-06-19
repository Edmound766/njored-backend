package com.njored.repositories

import com.njored.database.DatabaseFactory.dbQuery
import com.njored.database.tables.ProcessedWebhooksTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll

interface ProcessedWebhookRepository {
    suspend fun exists(messageId: String): Boolean
    suspend fun save(messageId: String)
}

class ProcessedWebhookRepositoryImpl : ProcessedWebhookRepository {

    override suspend fun exists(messageId: String): Boolean = dbQuery {
        ProcessedWebhooksTable
            .selectAll()
            .where { ProcessedWebhooksTable.webhookId eq messageId }
            .singleOrNull() != null
    }

    override suspend fun save(messageId: String): Unit = dbQuery {
        ProcessedWebhooksTable.insert {
            it[webhookId] = messageId
        }
    }
}