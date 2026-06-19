package com.njored.repositories

import com.njored.database.DatabaseFactory.dbQuery
import com.njored.database.tables.BusinessesTable
import com.njored.domain.Business
import com.njored.repositories.mappers.toBusiness
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insertReturning
import org.jetbrains.exposed.v1.jdbc.selectAll
import kotlin.uuid.Uuid

interface BusinessRepository {
    suspend fun create(name: String, contactEmail: String?): Business
    suspend fun findById(id: Uuid): Business?
}

class BusinessRepositoryImpl : BusinessRepository {

    override suspend fun create(name: String, contactEmail: String?): Business = dbQuery {
        BusinessesTable.insertReturning {
            it[BusinessesTable.name] = name
            it[BusinessesTable.contactEmail] = contactEmail
        }.single().toBusiness()
    }

    override suspend fun findById(id: Uuid): Business? = dbQuery {
        BusinessesTable
            .selectAll()
            .where { BusinessesTable.id eq id }
            .singleOrNull()
            ?.toBusiness()
    }
}