package com.njored.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Metadata(
    @SerialName("display_phone_number")
    val displayPhoneNumber: String,
    @SerialName("phone_number_id")
    val phoneNumberId: String
)