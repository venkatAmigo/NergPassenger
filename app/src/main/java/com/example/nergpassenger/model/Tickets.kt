package com.example.nergpassenger.model

import kotlinx.serialization.Serializable

@Serializable
class Tickets : ArrayList<Ticket>()

@Serializable
data class Ticket(
    val ticketNumber: String,
    val bookedAt: String,
    val travelDate: String,
    val passengers: List<String>,
    val type: String,
    val loyalty: String,
    val bookedBy: BookedBy,
    val segments: List<Segment>
)

@Serializable
data class BookedBy(
    val id: Int,
    val username: String,
    val givenName: String,
    val familyName: String,
    val birthdate: String,
    val cardType: String,
    val cardNumber: String?,
    val cardExpiration: String?,
    val cardSecurityCode: String?,
)
@Serializable
data class Segment(
    val id: Int,
    val departureStop: DepartureStop,
    val arrivalStop: ArrivalStop,
    val seatReservations: List<SeatReservations>
)

@Serializable
data class DepartureStop(
    val id: Int,
    val time: String,
    val delayInMinutes: Int,
    val station: Station,
    val connection: Connection,
)
@Serializable
data class ArrivalStop(
    val id: Int,
    val time: String,
    val delayInMinutes: Int,
    val station: Station,
    val connection: Connection,
)
@Serializable
data class Station(
    val code: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
)
@Serializable
data class Connection(
    val trainNumber: String
)
@Serializable
data class SeatReservations(
    val id: Int,
    val railroadCarNumber: Int,
    val seatNumber: String,
)