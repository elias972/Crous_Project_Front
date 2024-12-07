// CrousAdapter.kt
package com.example.crous_project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CrousAdapter(
    private var crousList: List<Crous>,
    private val onItemClick: (Crous) -> Unit,
    private val onFavoriteClick: (Crous) -> Unit
) : RecyclerView.Adapter<CrousViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrousViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_crous, parent, false)
        return CrousViewHolder(view)
    }

    override fun onBindViewHolder(holder: CrousViewHolder, position: Int) {
        val crous = crousList[position]
        holder.bind(crous, onFavoriteClick)
        holder.itemView.setOnClickListener { onItemClick(crous) }
    }

    override fun getItemCount(): Int = crousList.size

    fun updateCrousList(newList: List<Crous>) {
        crousList = newList
        notifyDataSetChanged()
    }
}
