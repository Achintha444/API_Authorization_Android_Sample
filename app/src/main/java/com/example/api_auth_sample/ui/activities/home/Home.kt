package com.example.api_auth_sample.ui.activities.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.api_auth_sample.ui.activities.main.MainActivity
import com.example.api_auth_sample.R
import com.example.api_auth_sample.api.DataSource
import com.example.api_auth_sample.api.data_source.pet.PetAPI
import com.example.api_auth_sample.databinding.ActivityHomeBinding
import com.example.api_auth_sample.model.api.data_source.pet.GetAllPetsCallback
import com.example.api_auth_sample.model.data.Pet
import com.example.api_auth_sample.model.util.uiUtil.SharedPreferencesKeys
import com.example.api_auth_sample.util.UiUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var signoutButton: ImageButton
    private lateinit var petsRecyclerView: RecyclerView

    private lateinit var pets: ArrayList<Pet>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeComponents()

        // hide action bar and status bar
        UiUtil.hideStatusBar(window, resources, theme, R.color.asgardeo_secondary)

        //setPetsCardAdapter()
        setPets()

        setSignOutButtonOnClick()
    }

    private fun initializeComponents() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signoutButton = findViewById(R.id.signOutButton)
        petsRecyclerView = findViewById(R.id.petsRecyclerView)

    }

    private fun setPets() {
        PetAPI.getPets(
            applicationContext
//            GetAllPetsCallback(
//                onSuccess = { pets ->
//                    this.pets = pets
//                },
//                onError = { },
//                onFinally = { }
//                onWaiting = { }
//
//            )
        )
    }

//    private fun setPetsCardAdapter() {
//        val cardAdapter = CardAdapter([])
//        petsRecyclerView.layoutManager = LinearLayoutManager(this)
//        petsRecyclerView.adapter = cardAdapter
//    }

    private fun setSignOutButtonOnClick() {
        signoutButton.setOnClickListener {
            // Sign out from google if the user is signed in from google
            GoogleSignIn.getClient(this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                .signOut()

            // Clear the access token from the shared preferences
            UiUtil.writeToSharedPreferences(
                applicationContext.getSharedPreferences(R.string.app_name.toString(),
                    Context.MODE_PRIVATE), SharedPreferencesKeys.ACCESS_TOKEN.key, "")

            // Redirect to the login page
            val intent = Intent(this@Home, MainActivity::class.java)
            startActivity(intent)
        }
    }
}