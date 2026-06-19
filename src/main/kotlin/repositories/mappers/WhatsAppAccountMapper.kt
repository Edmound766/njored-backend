package com.njored.repositories.mappers

import com.njored.database.tables.WhatsAppAccountsTable
import com.njored.domain.WhatsAppAccount
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toWhatsAppAccount(): WhatsAppAccount = WhatsAppAccount(
    id = this[WhatsAppAccountsTable.id].value,
    businessId = this[WhatsAppAccountsTable.businessId].value,
    phoneNumberId = this[WhatsAppAccountsTable.phoneNumber],
    businessAccountId = this[WhatsAppAccountsTable.businessAccountId],
    accessToken = this[WhatsAppAccountsTable.accessToken],
    webhookVerifyToken = this[WhatsAppAccountsTable.webhookVerifyToken],
)