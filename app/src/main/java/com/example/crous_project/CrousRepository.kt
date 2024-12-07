// CrousRepository.kt
package com.example.crous_project

import android.content.Context

object CrousRepository {
    private val storage = mutableListOf<Crous>()

    fun addCrous(crous: Crous) {
        storage.add(crous)
    }

    fun getCrous(id: String): Crous? {
        return storage.find { it.id == id }
    }

    fun getAllCrous(): List<Crous> {
        return storage.sortedWith(compareByDescending<Crous> { it.favorite }.thenBy { it.nom })
    }

    fun updateFavoriteStatus(id: String, favorite: Boolean, context: Context) {
        val crous = getCrous(id)
        if (crous != null) {
            crous.favorite = favorite
            saveFavorites(context)
        }
    }

    fun loadFavorites(context: Context) {
        val sharedPrefs = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val favoriteIds = sharedPrefs.getStringSet("favorite_ids", emptySet()) ?: emptySet()
        storage.forEach { it.favorite = it.id in favoriteIds }
    }
    fun saveFavorites(context: Context) {
        val sharedPrefs = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        val favoriteIds = storage.filter { it.favorite }.map { it.id }.toSet()
        editor.putStringSet("favorite_ids", favoriteIds)
        editor.apply()
    }

    fun clear() {
        storage.clear()
    }
}
