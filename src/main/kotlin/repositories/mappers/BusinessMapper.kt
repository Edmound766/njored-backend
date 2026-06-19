package com.njored.repositories.mappers

import com.njored.database.tables.BusinessesTable
import com.njored.domain.Business
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toBusiness(): Business = Business(
    id = this[BusinessesTable.id].value,
    name = this[BusinessesTable.name],
    contactEmail = this[BusinessesTable.contactEmail],
)