//// AddActivity.kt
//package com.example.crous_project
//
//import kotlin.random.Random
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import androidx.appcompat.app.AppCompatActivity
//import android.widget.ArrayAdapter
//import android.widget.Spinner
//
//class CrousAddActivity : AppCompatActivity() {
//
//    private lateinit var edtNom: EditText
//    private lateinit var spinnerType: Spinner
//    private lateinit var edtZone: EditText
//    private lateinit var edtDescription: EditText
//    private lateinit var edtContact: EditText
//    private lateinit var edtInfo: EditText
//    private lateinit var edtPhoto: EditText
//    private lateinit var edtLatitude: EditText
//    private lateinit var edtLongitude: EditText
//    private lateinit var btnAdd: Button
//    private lateinit var btnReturnToMain: Button
//
//    private val crousRepository = CrousRepository
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add)
//
//        // selector for the type Restaurant
//        //Cafétéria
//        //Pizzéria
//        //Restaurant agréé
//        //Libre-service
//        //Kiosque
//        //Brasserie
//
//        // Zone ex: Marseille
//
//        // Contact -> email, phoneNo, name
//
//        // Missing:
//        // info
//        // geolocalisation
//        // photo url?
//
//        // Initialize UI components
//        edtNom = findViewById(R.id.edtNom)
//        spinnerType = findViewById(R.id.spinnerType)
//        edtZone = findViewById(R.id.edtZone)
//        edtDescription = findViewById(R.id.edtDescription)
//        edtContact = findViewById(R.id.edtContact)
//        edtInfo = findViewById(R.id.edtInfo)
//        edtPhoto = findViewById(R.id.edtPhoto)
//        edtLatitude = findViewById(R.id.edtLatitude)
//        edtLongitude = findViewById(R.id.edtLongitude)
//        btnAdd = findViewById(R.id.btnAdd)
//        btnReturnToMain = findViewById(R.id.btnReturnToMain)
//
//        // Set up the selector
//        val typeOptions = resources.getStringArray(R.array.type_options)
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, typeOptions)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerType.adapter = adapter
//
//        // Handle Add button click
//        btnAdd.setOnClickListener {
//            val selectedType = spinnerType.selectedItem.toString()
//
//            val latitude = edtLatitude.text.toString().toDoubleOrNull()
//            val longitude = edtLongitude.text.toString().toDoubleOrNull()
//
//            val geolocalisation = if (latitude != null && longitude != null) {
//                Geolocalisation(latitude, longitude)
//            } else {
//                null
//            }
//
//            val crous = Crous(
//                id = generateNewId(),
//                nom = edtNom.text.toString(),
//                type = selectedType,
//                zone = edtZone.text.toString(),
//                description = edtDescription.text.toString(),
//                contact = edtContact.text.toString(),
//                geolocalisation = geolocalisation,
//                photo = edtPhoto.text.toString(),
//                info = edtInfo.text.toString(),
//                favorite = false
//            )
//            crousRepository.addCrous(crous)
//            crousRepository.saveFavorites(this)
//            finish()
//        }
//
//
//        // Return to main menu
//        btnReturnToMain.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java) // Replace MainActivity with your actual main activity class
//            startActivity(intent)
//        }
//    }
//
//    private fun generateNewId(): String {
//        return Random.nextInt(100000, 999999).toString()
//    }
//}
