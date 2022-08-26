package com.example.nergpassenger.model

import kotlinx.serialization.Serializable

@Serializable
data class ReservedSeat(
    val railroadCarNumber: Int,
    val seatNumber: String
)
