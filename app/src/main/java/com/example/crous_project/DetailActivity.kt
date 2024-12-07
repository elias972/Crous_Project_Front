// DetailActivity.kt
package com.example.crous_project

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private lateinit var crous: Crous

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Retrieve the Crous object from the intent
        crous = intent.getSerializableExtra("crous") as Crous

        // Initialize UI components
        val imageViewPhoto = findViewById<ImageView>(R.id.imageViewPhoto)
        val textViewNom = findViewById<TextView>(R.id.textViewNom)
        val textViewType = findViewById<TextView>(R.id.textViewType)
        val textViewZone = findViewById<TextView>(R.id.textViewZone)
        val textViewDescription = findViewById<TextView>(R.id.textViewDescription)
        val textViewContact = findViewById<TextView>(R.id.textViewContact)
        val textViewInformations = findViewById<TextView>(R.id.textViewInformations)
        // Add other UI components as needed

        // Set data to UI components
        textViewNom.text = crous.nom
        textViewType.text = crous.type
        textViewZone.text = crous.zone
        textViewDescription.text = crous.description ?: "No description available"
        textViewContact.text = "Contact: ${crous.contact ?: "N/A"}"
        textViewInformations.text = crous.informations ?: "No additional information"

        // Load photo using Glide or any other image loading library
        if (!crous.photo.isNullOrEmpty()) {
            Glide.with(this)
                .load(crous.photo)
                .placeholder(R.drawable.ic_placeholder) // Provide a placeholder image
                .into(imageViewPhoto)
        } else {
            // Set a default image or hide the ImageView
            imageViewPhoto.setImageResource(R.drawable.ic_placeholder)
        }

        // Optionally, set up the action bar
        supportActionBar?.title = crous.nom
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Handle the up (back) button in the action bar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
