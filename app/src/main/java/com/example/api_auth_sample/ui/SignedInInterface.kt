package com.example.api_auth_sample.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.api_auth_sample.MainActivity
import com.example.api_auth_sample.R
import com.example.api_auth_sample.databinding.ActivitySignedInInterfaceBinding

class SignedInInterface : AppCompatActivity() {

    private lateinit var binding: ActivitySignedInInterfaceBinding
    private lateinit var signoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeComponents()

        signoutButton.setOnClickListener{
            val intent = Intent(this@SignedInInterface, MainActivity::class.java);
            startActivity(intent)
        }
    }

    private fun initializeComponents() {
        binding = ActivitySignedInInterfaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signoutButton = findViewById(R.id.signOutButton);
    }
}