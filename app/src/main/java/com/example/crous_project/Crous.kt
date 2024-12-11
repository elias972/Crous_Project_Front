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
    val info: String?,
    val geolocalisation: Geolocalisation?, // [latitude, longitude]
    val photo: String?,
    var favorite: Boolean
) : Serializable
