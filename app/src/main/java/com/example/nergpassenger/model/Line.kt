package com.example.nergpassenger.model

import kotlinx.serialization.Serializable

@Serializable
data class Line(
    val id: String,
    val name: String
)
