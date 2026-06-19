package com.njored.routes

import com.njored.services.LeadService
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlin.uuid.Uuid

fun Route.leadRoutes(leadService: LeadService) {
    get("/leads/{id}") {
        val idParam = call.parameters["id"]
        val id = idParam?.let {
            try {
                Uuid.parse(it)
            } catch (e: IllegalArgumentException) {
                null
            }
        }

        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid lead id")
            return@get
        }

        val lead = leadService.getLeadById(id)

        if (lead == null) {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }

        call.respond(lead)
    }
}