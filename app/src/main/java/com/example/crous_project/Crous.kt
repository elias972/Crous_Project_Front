// Crous.kt
package com.example.crous_project

import java.io.Serializable

data class Crous(
    val id: String,
    val type: String,
    val zone: String,
    val nom: String,
    val description: String?,
    val contact: String?,
    val lat: Double?,
    val informations: String?,
    val closing: String?,
    val geolocalisation: List<Double>?, // [latitude, longitude]
    val zone2: String?,
    val crousandgo: String?,
    val album: String?,
    val photo: String?,
    var favorite: Boolean = false // Favorite status
) : Serializable
