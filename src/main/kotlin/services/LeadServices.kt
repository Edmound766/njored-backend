package com.njored.services

import com.njored.domain.Lead
import com.njored.repositories.LeadRepository
import kotlin.uuid.Uuid

class LeadService(
    private val leadRepository: LeadRepository
) {

    suspend fun getLeadById(id: Uuid): Lead? {
        return leadRepository.findById(id)
    }
}