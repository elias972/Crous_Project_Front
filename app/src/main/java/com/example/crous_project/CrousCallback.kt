package com.example.crous_project

import android.content.Context

interface CrousCallback {
    fun onFavoriteToggled(crousId: String, isFavorite: Boolean)
}