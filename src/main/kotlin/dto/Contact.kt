package com.njored.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Contact(
    val profile: Profile,
    @SerialName("wa_id")
    val waId: String
)