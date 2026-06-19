package com.njored.services

import com.njored.database.enums.LeadStatus
import com.njored.repositories.LeadRepository
import com.njored.repositories.LeadStatusHistoryRepository
import kotlin.uuid.Uuid

interface LeadStatusService {
    suspend fun transitionStatus(
        leadId: Uuid,
        oldStatus: LeadStatus?,
        newStatus: LeadStatus,
        reason: String? = null
    )
}

class LeadStatusServiceImpl(
    private val leadRepository: LeadRepository,
    private val leadStatusHistoryRepository: LeadStatusHistoryRepository
) : LeadStatusService {

    override suspend fun transitionStatus(
        leadId: Uuid,
        oldStatus: LeadStatus?,
        newStatus: LeadStatus,
        reason: String?
    ) {
        leadRepository.updateStatus(leadId, newStatus)
        leadStatusHistoryRepository.record(
            leadId = leadId,
            oldStatus = oldStatus,
            newStatus = newStatus,
            reason = reason
        )
    }
}