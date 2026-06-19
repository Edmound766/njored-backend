package com.njored.services

import com.njored.domain.Agent
import com.njored.repositories.AgentRepository
import com.njored.repositories.LeadRepository
import java.time.OffsetDateTime
import kotlin.uuid.Uuid

class AssignmentService(
    private val agentRepository: AgentRepository,
    private val leadRepository: LeadRepository
) {

    suspend fun assignLead(businessId: Uuid, leadId: Uuid): Agent {
        val activeAgents = agentRepository.findActiveAgents(businessId)

        require(activeAgents.isNotEmpty()) {
            "No active agents available for business $businessId"
        }

        // Round-robin: pick the agent who was assigned longest ago (or never)
        val nextAgent = activeAgents.minByOrNull { it.lastAssignmentAt?: OffsetDateTime.now() }
            ?: activeAgents.first()

        leadRepository.assignAgent(leadId, nextAgent.id)
        agentRepository.updateLastAssignment(nextAgent.id)

        return nextAgent
    }
}