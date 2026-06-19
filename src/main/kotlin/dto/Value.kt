package com.njored.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Value(
    @SerialName("messaging_product")
    val messagingProduct: String,
    val metadata: Metadata,
    val contacts: List<Contact> = emptyList(),
    val messages: List<Message> = emptyList()
)