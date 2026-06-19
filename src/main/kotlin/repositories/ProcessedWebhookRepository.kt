package com.njored.repositories

import com.njored.database.DatabaseFactory.dbQuery
import com.njored.database.tables.WhatsAppAccountsTable
import com.njored.domain.WhatsAppAccount
import com.njored.repositories.mappers.toWhatsAppAccount
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insertReturning
import org.jetbrains.exposed.v1.jdbc.selectAll
import kotlin.uuid.Uuid

interface WhatsAppAccountRepository {
    suspend fun create(
        businessId: Uuid,
        phoneNumberId: String,
        businessAccountId: String,
        accessToken: String,
        webhookVerifyToken: String
    ): WhatsAppAccount

    suspend fun findById(id: Uuid): WhatsAppAccount?
    suspend fun findByPhoneNumberId(phoneNumberId: String): WhatsAppAccount?
    suspend fun findByBusinessId(businessId: Uuid): WhatsAppAccount?
    suspend fun findByVerifyToken(token: String): WhatsAppAccount?
}

class WhatsAppAccountRepositoryImpl : WhatsAppAccountRepository {

    override suspend fun create(
        businessId: Uuid,
        phoneNumberId: String,
        businessAccountId: String,
        accessToken: String,
        webhookVerifyToken: String
    ): WhatsAppAccount = dbQuery {
        WhatsAppAccountsTable.insertReturning {
            it[WhatsAppAccountsTable.businessId] = businessId
            it[phoneNumber] = phoneNumberId
            it[WhatsAppAccountsTable.businessAccountId] = businessAccountId
            it[WhatsAppAccountsTable.accessToken] = accessToken
            it[WhatsAppAccountsTable.webhookVerifyToken] = webhookVerifyToken
        }.single().toWhatsAppAccount()
    }

    override suspend fun findById(id: Uuid): WhatsAppAccount? = dbQuery {
        WhatsAppAccountsTable
            .selectAll()
            .where { WhatsAppAccountsTable.id eq id }
            .singleOrNull()
            ?.toWhatsAppAccount()
    }

    override suspend fun findByPhoneNumberId(phoneNumberId: String): WhatsAppAccount? = dbQuery {
        WhatsAppAccountsTable
            .selectAll()
            .where { WhatsAppAccountsTable.phoneNumber eq phoneNumberId }
            .singleOrNull()
            ?.toWhatsAppAccount()
    }

    override suspend fun findByBusinessId(businessId: Uuid): WhatsAppAccount? = dbQuery {
        WhatsAppAccountsTable
            .selectAll()
            .where { WhatsAppAccountsTable.businessId eq businessId }
            .singleOrNull()
            ?.toWhatsAppAccount()
    }

    override suspend fun findByVerifyToken(token: String): WhatsAppAccount? = dbQuery {
        WhatsAppAccountsTable
            .selectAll()
            .where { WhatsAppAccountsTable.webhookVerifyToken eq token }
            .singleOrNull()
            ?.toWhatsAppAccount()
    }
}