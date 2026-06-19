package com.njored

import com.njored.repositories.AgentRepositoryImpl
import com.njored.repositories.BusinessRepositoryImpl
import com.njored.repositories.ConversationRepositoryImpl
import com.njored.repositories.LeadRepositoryImpl
import com.njored.repositories.LeadStatusHistoryRepositoryImpl
import com.njored.repositories.ProcessedWebhookRepositoryImpl
import com.njored.repositories.WhatsAppAccountRepositoryImpl
import com.njored.services.AssignmentService
import com.njored.services.ConversationServiceImpl
import com.njored.services.LeadIngestionService
import com.njored.services.LeadService
import com.njored.services.LeadStatusServiceImpl
import com.njored.services.MetaWhatsAppClient
import com.njored.webhooks.whatsAppWebhook
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    // Repositories
    val leadRepository = LeadRepositoryImpl()
    val agentRepository = AgentRepositoryImpl()
    val conversationRepository = ConversationRepositoryImpl()
    val businessRepository = BusinessRepositoryImpl()
    val leadStatusHistoryRepository = LeadStatusHistoryRepositoryImpl()
    val whatsappAccountRepository = WhatsAppAccountRepositoryImpl()
    val processedWebhookRepository = ProcessedWebhookRepositoryImpl()

    val httpClient = HttpClient(CIO){
        install(ContentNegotiation) {
            json()
        }
    }

    val whatsAppClient = MetaWhatsAppClient(httpClient)

    // Services
    val leadService = LeadService(leadRepository)
    val conversationService = ConversationServiceImpl(conversationRepository)
    val assignmentService = AssignmentService(agentRepository, leadRepository)
    val leadStatusService = LeadStatusServiceImpl(leadRepository, leadStatusHistoryRepository)

    val leadIngestionService = LeadIngestionService(
        leadRepository,
        conversationService,
        assignmentService,
        whatsAppClient
    )
    routing {
        whatsAppWebhook(
            leadIngestionService,
            whatsappAccountRepository,
            processedWebhookRepository
        )

        get("/") {
            call.respondText("Hello, World!")
        }
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}