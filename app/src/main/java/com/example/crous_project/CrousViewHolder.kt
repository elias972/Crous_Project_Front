// CrousViewHolder.kt
package com.example.crous_project

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CrousViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imgPhoto: ImageView = itemView.findViewById(R.id.row_crous_img_photo)
    private val txvNom: TextView = itemView.findViewById(R.id.row_crous_txv_nom)
    private val txvZone: TextView = itemView.findViewById(R.id.row_crous_txv_zone)
    private val imgFavorite: ImageView = itemView.findViewById(R.id.row_crous_img_favorite)

    fun bind(crous: Crous, onFavoriteClick: (Crous) -> Unit) {
        txvNom.text = crous.nom
        txvZone.text = crous.zone

        // Load photo using Glide
        Glide.with(itemView.context)
            .load(crous.photo)
            .placeholder(R.drawable.ic_placeholder)
            .into(imgPhoto)

        updateFavoriteIcon(crous.favorite)

        imgFavorite.setOnClickListener {
            onFavoriteClick(crous)
            updateFavoriteIcon(crous.favorite)
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconRes = if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        imgFavorite.setImageResource(iconRes)
    }
}
