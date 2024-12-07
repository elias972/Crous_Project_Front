// AddActivity.kt
package com.example.crous_project

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class CrousAddActivity : AppCompatActivity() {

    private lateinit var edtNom: EditText
    private lateinit var edtType: EditText
    private lateinit var edtZone: EditText
    private lateinit var edtDescription: EditText
    private lateinit var edtContact: EditText
    private lateinit var btnAdd: Button

    private val crousRepository = CrousRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        // Initialize UI components
        edtNom = findViewById(R.id.edtNom)
        edtType = findViewById(R.id.edtType)
        edtZone = findViewById(R.id.edtZone)
        edtDescription = findViewById(R.id.edtDescription)
        edtContact = findViewById(R.id.edtContact)
        btnAdd = findViewById(R.id.btnAdd)

        // Handle Add button click
        btnAdd.setOnClickListener {
            val crous = Crous(
                id = generateNewId(),
                nom = edtNom.text.toString(),
                type = edtType.text.toString(),
                zone = edtZone.text.toString(),
                description = edtDescription.text.toString(),
                contact = edtContact.text.toString(),
                lat = null,
                informations = null,
                closing = null,
                geolocalisation = null,
                zone2 = null,
                crousandgo = null,
                album = null,
                photo = "",
                favorite = false
            )
            crousRepository.addCrous(crous)
            crousRepository.saveFavorites(this)
            finish()
        }
    }

    private fun generateNewId(): String {
        return (crousRepository.getAllCrous().size + 1).toString()
    }
}
