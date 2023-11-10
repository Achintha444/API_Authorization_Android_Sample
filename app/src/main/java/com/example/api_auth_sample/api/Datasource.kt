package com.example.api_auth_sample.api

import com.example.api_auth_sample.ui.data.Doctor
import com.example.api_auth_sample.ui.data.Pharmacy

class DataSource {
    companion object {
        fun getDummyDoctors(): List<Doctor> {
            return listOf(
                Doctor("Dr. Smith", "Cardiologist", "Hospital A"),
                Doctor("Dr. Johnson", "Dermatologist", "Hospital B"),
                Doctor("Dr. Williams", "Pediatrician", "Hospital C")
            )
        }

        fun getDummyPharmacies(): List<Pharmacy> {
            return listOf(
                Pharmacy("ABC Pharmacy", "Your Friendly Neighborhood Pharmacy", "123 Main St"),
                Pharmacy("XYZ Pharmacy", "Health First Pharmacy", "456 Oak St"),
                Pharmacy("City Pharmacy", "City Center Pharmacy", "789 Maple St")
            )
        }
    }
}
