package com.example.crous_project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlin.random.Random

class CrousAddFragment : Fragment() {
    private lateinit var edtNom: EditText
    private lateinit var spinnerType: Spinner
    private lateinit var edtZone: EditText
    private lateinit var edtDescription: EditText
    private lateinit var edtContact: EditText
    private lateinit var edtInfo: EditText
    private lateinit var edtPhoto: EditText
    private lateinit var edtLatitude: EditText
    private lateinit var edtLongitude: EditText
    private lateinit var btnAdd: Button
    private lateinit var listener: CrousCreator
    private lateinit var btnReturnToMain: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_add, container, false)

        // Initialize UI components
        edtNom = rootView.findViewById(R.id.edtNom)
        spinnerType = rootView.findViewById(R.id.spinnerType)
        edtZone = rootView.findViewById(R.id.edtZone)
        edtDescription = rootView.findViewById(R.id.edtDescription)
        edtContact = rootView.findViewById(R.id.edtContact)
        edtInfo = rootView.findViewById(R.id.edtInfo)
        edtPhoto = rootView.findViewById(R.id.edtPhoto)
        edtLatitude = rootView.findViewById(R.id.edtLatitude)
        edtLongitude = rootView.findViewById(R.id.edtLongitude)
        btnAdd = rootView.findViewById(R.id.btnAdd)
        btnReturnToMain = rootView.findViewById(R.id.btnReturnToMain)

        val typeOptions = resources.getStringArray(R.array.type_options)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, typeOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = adapter

        // Add button click
        btnAdd.setOnClickListener {
            if (validateInputs()) {
                saveCrous()
            }
        }

        // Return to main page
        btnReturnToMain.setOnClickListener {
            navigateToMainMenu()
        }

        return rootView
    }

    private fun navigateToMainMenu() {
        activity?.let {
            val intent = Intent(it, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            it.finish() // Optional: finish the current activity hosting the fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CrousCreator) {
            listener = context
        } else {
            throw RuntimeException("$context must implement CrousCreator")
        }
    }

    /**
     * Validates user inputs. Shows Toast messages if validations fail.
     * Returns true if all validations pass, false otherwise.
     */
    private fun validateInputs(): Boolean {
        // Check mandatory fields
        val nom = edtNom.text.toString().trim()
        val type = spinnerType.selectedItem?.toString()?.trim()
        val zone = edtZone.text.toString().trim()

        if (nom.isEmpty()) {
            showToast("Please enter the restaurant name.")
            return false
        }

        if (type.isNullOrEmpty()) {
            showToast("Please select a type.")
            return false
        }

        if (zone.isEmpty()) {
            showToast("Please enter the zone.")
            return false
        }

        // Validate latitude and longitude if provided
        val latitudeStr = edtLatitude.text.toString().trim()
        val longitudeStr = edtLongitude.text.toString().trim()

        if (latitudeStr.isNotEmpty()) {
            val latVal = latitudeStr.toDoubleOrNull()
            if (latVal == null) {
                showToast("Invalid latitude format. Please enter a number.")
                return false
            }
        }

        if (longitudeStr.isNotEmpty()) {
            val lonVal = longitudeStr.toDoubleOrNull()
            if (lonVal == null) {
                showToast("Invalid longitude format. Please enter a number.")
                return false
            }
        }

        return true
    }

    private fun saveCrous() {
        val selectedType = spinnerType.selectedItem.toString()
        val latitude = edtLatitude.text.toString().toDoubleOrNull()
        val longitude = edtLongitude.text.toString().toDoubleOrNull()

        val geolocalisation = if (latitude != null && longitude != null) {
            Geolocalisation(latitude, longitude)
        } else {
            null
        }

        val crous = Crous(
            id = generateNewId(),
            nom = edtNom.text.toString().trim(),
            type = selectedType,
            zone = edtZone.text.toString().trim(),
            description = edtDescription.text.toString().trim(),
            contact = edtContact.text.toString().trim(),
            geolocalisation = geolocalisation,
            photo = edtPhoto.text.toString().trim(),
            info = edtInfo.text.toString().trim(),
            favorite = false
        )

        listener.onCrousCreated(crous)
        showToast("Crous added successfully!")
    }

    private fun generateNewId(): String {
        return Random.nextInt(100000, 999999).toString()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
